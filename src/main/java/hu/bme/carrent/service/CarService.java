package hu.bme.carrent.service;

import hu.bme.carrent.dto.*;
import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Reservation;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.CarRepository;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import hu.bme.carrent.util.Converter;
import hu.bme.carrent.util.ReservationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private Converter converter;

    public UserDTO addCarToUser(CarRequest carRequest) {
        User owner = userRepository.findById(carRequest.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner is not an existing user"));
        Car car = converter.convertToEntity(carRequest, owner);
        owner.getCars().add(car);
        return converter.convertToDTO(userRepository.save(owner));
    }

    @Transactional
    public UserDTO deleteCar(Long carId) {
        reservationRepository.deleteAllByCarId(carId);

        User user = userRepository.findUserByCarsId(carId);
        user.getCars().removeIf(c -> c.getId().longValue() == carId.longValue());

        return converter.convertToDTO(userRepository.save(user));
    }

    public UserDTO updateCar(CarDTO carDTO) {
        User user = userRepository.findUserByCarsId(carDTO.getId());
        user.getCars().removeIf(c -> c.getId().longValue() == carDTO.getId().longValue());
        Car car = converter.convertToEntity(carDTO, user);
        car.setId(carDTO.getId());
        user.getCars().add(car);
        return converter.convertToDTO(userRepository.save(user));
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(c -> converter.convertToDTO(c))
                .collect(Collectors.toList());
    }

    public List<CarDTO> getOwnedCars(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists"));
        return user.getCars().stream()
                .map(c -> converter.convertToDTO(c))
                .collect(Collectors.toList());
    }

    public CarDetailDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car does not exists"));
        User owner = car.getOwner();
        ReducedUserDTO ownerDTO = new ReducedUserDTO(owner.getId(), owner.getUsername());
        return new CarDetailDTO(car.getId(), car.getType(), car.getCity(), car.getCapacity(), ownerDTO);
    }

    public List<CarDTO> searchFreeCarsByCityByStartByEnd(String city, Date start, Date end) {
        List<Car> carsInTheCity = carRepository.findCarsByCity(city);
        if (carsInTheCity.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("Cars by city: {}, cars: {}", city, carsInTheCity);
        List<Reservation> reservations = reservationRepository.findReservationsByCarCity(city);
        List<Long> reservedCarIds = reservations.stream()
                .filter(r -> ReservationValidator.isReservationAcceptable(start, end, r))
                .map(r -> r.getCar().getId()).collect(Collectors.toList());
        log.info("Reserved car ids: {}", reservedCarIds);
        if (reservedCarIds.isEmpty()) {
            return carsInTheCity.stream()
                    .map(c -> converter.convertToDTO(c))
                    .collect(Collectors.toList());
        } else {
            return carsInTheCity.stream().filter(c -> reservedCarIds.contains(c.getId()))
                    .map(c -> converter.convertToDTO(c))
                    .collect(Collectors.toList());
        }
    }

}
