package hu.bme.carrent.service;

import hu.bme.carrent.dto.*;
import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Reservation;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.CarRepository;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarRentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findUserByUsername(userDTO.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        } else {
            User user = convertToEntity(userDTO);
            return convertToDTO(userRepository.save(user));
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if (!userRepository.findById(userDTO.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists");
        } else {
            User user = convertToEntity(userDTO);
            return convertToDTO(userRepository.save(user));
        }
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists"));

        userRepository.delete(user);
    }

    public UserDTO addCarToUser(CarRequest carRequest) {
        User owner = userRepository.findById(carRequest.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner is not an existing user"));
        Car car = new Car(carRequest.getType(), carRequest.getCity(), carRequest.getCapacity(), owner);
        owner.getCars().add(car);
        return convertToDTO(userRepository.save(owner));
    }

    public UserDTO deleteCar(Long carId) {
        User user = userRepository.findUserByCarsId(carId);
        user.getCars().removeIf(c -> c.getId() == carId);
        return convertToDTO(userRepository.save(user));
    }

    public UserDTO updateCar(CarDTO carDTO) {
        User user = userRepository.findUserByCarsId(carDTO.getId());
        user.getCars().removeIf(c -> c.getId() == carDTO.getId());
        user.getCars().add(convertToEntity(carDTO));
        return convertToDTO(userRepository.save(user));
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream().map(c -> convertToDTO(c)).collect(Collectors.toList());
    }

    public List<CarDTO> getOwnedCars(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists"));
        return user.getCars().stream().map(c -> convertToDTO(c)).collect(Collectors.toList());
    }

    public CarDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car does not exists"));
        return convertToDTO(car);
    }

    public List<CarDTO> searchFreeCarsByCityByStartByEnd(String city, Date start, Date end) {
        List<Car> carsInTheCity = carRepository.findCarsByCity(city);
        if (carsInTheCity.isEmpty()) {
            return Collections.emptyList();
        }
        List<Reservation> reservations = reservationRepository.findReservationsByCarCity(city);
        List<Long> reservedCarIds = reservations.stream()
                .filter(r -> !isReservationAcceptable(start, end, r))
                .map(r -> r.getCar().getId()).collect(Collectors.toList());
        List<CarDTO> result = carsInTheCity.stream().filter(c -> reservedCarIds.contains(c.getId()))
                .map(c -> convertToDTO(c))
                .collect(Collectors.toList());

        return result;
    }

    public ReservationDTO createReservation(ReservationRequest reservationRequest) {
        if (reservationRequest.getEnd().before(reservationRequest.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        } else {
            List<Reservation> existingReservations = reservationRepository
                    .findReservationsByCarId(reservationRequest.getCarId());
            if (isReservationAcceptable(reservationRequest.getStart(), reservationRequest.getEnd(), existingReservations)) {
                Car car = carRepository.findById(reservationRequest.getCarId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not existing car"));
                User user = userRepository.findById(reservationRequest.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not existing user"));
                Reservation reservation = new Reservation(car, user, reservationRequest.getStart(), reservationRequest.getEnd());
                return convertToDTO(reservationRepository.save(reservation));

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The required time period is reserved");
            }
        }
    }

    public List<ReservationDTO> getOwnedReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByUserId(userId);
        return reservations.stream().map(r -> convertToDTO(r)).collect(Collectors.toList());
    }

    public List<ReservationDTO> reservationsForOwnedCars(Long userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByCarOwnerId(userId);
        return reservations.stream().map(r -> convertToDTO(r)).collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "REservation does not exists"));
        return convertToDTO(reservation);
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    private boolean isReservationAcceptable(Date newStart, Date newEnd, List<Reservation> oldReservations) {
        for (Reservation reservation : oldReservations) {
            if (newStart.after(reservation.getStart()) && newStart.before(reservation.getEnd())) {
                return false;
            }
            if (newEnd.after(reservation.getStart()) && newEnd.before(reservation.getEnd())) {
                return false;
            }
            if (newStart.before(reservation.getStart()) && newEnd.after(reservation.getEnd())) {
                return false;
            }
        }
        return true;
    }

    private boolean isReservationAcceptable(Date start, Date end, Reservation reservation) {
        if (start.after(reservation.getStart()) && start.before(reservation.getEnd())) {
            return false;
        }
        if (end.after(reservation.getStart()) && end.before(reservation.getEnd())) {
            return false;
        }
        if (start.before(reservation.getStart()) && end.after(reservation.getEnd())) {
            return false;
        }
        return true;
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private Car convertToEntity(CarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    private CarDTO convertToDTO(Car car) {
        return modelMapper.map(car, CarDTO.class);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDTO.class);
    }
}
