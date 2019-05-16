package hu.bme.carrent.service;

import hu.bme.carrent.dto.ReservationDTO;
import hu.bme.carrent.dto.ReservationRequest;
import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Reservation;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.CarRepository;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import hu.bme.carrent.util.Converter;
import hu.bme.carrent.util.ReservationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private Converter converter;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public ReservationDTO createReservation(ReservationRequest reservationRequest) {
        if (reservationRequest.getEnd().before(reservationRequest.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        } else {
            List<Reservation> existingReservations = reservationRepository
                    .findReservationsByCarId(reservationRequest.getCarId());
            if (ReservationValidator.isReservationAcceptable(reservationRequest.getStart(), reservationRequest.getEnd(), existingReservations)) {
                Car car = carRepository.findById(reservationRequest.getCarId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not existing car"));
                User user = userRepository.findById(reservationRequest.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not existing user"));
                Reservation reservation = new Reservation(car, user, reservationRequest.getStart(), reservationRequest.getEnd());
                return converter.convertToDTO(reservationRepository.save(reservation));

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The required time period is reserved");
            }
        }
    }

    public List<ReservationDTO> getOwnedReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByUserId(userId);
        return reservations.stream().map(r -> converter.convertToDTO(r)).collect(Collectors.toList());
    }

    public List<ReservationDTO> reservationsForOwnedCars(Long userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByCarOwnerId(userId);
        return reservations.stream().map(r -> converter.convertToDTO(r)).collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation does not exists"));
        return converter.convertToDTO(reservation);
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
