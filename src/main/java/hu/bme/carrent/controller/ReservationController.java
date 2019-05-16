package hu.bme.carrent.controller;

import hu.bme.carrent.dto.ReservationDTO;
import hu.bme.carrent.dto.ReservationRequest;
import hu.bme.carrent.service.CarService;
import hu.bme.carrent.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.createReservation(reservationRequest);
    }

    @GetMapping(path = "/owned/{userid}")
    public List<ReservationDTO> getOwnedReservations(@PathVariable("userid") Long userid) {
        return reservationService.getOwnedReservations(userid);
    }

    @GetMapping(path = "/cars/{userid}")
    public List<ReservationDTO> getReservationsForOwnedCars(@PathVariable("userid") Long userid) {
        return reservationService.reservationsForOwnedCars(userid);
    }

    @GetMapping(path = "/{id}")
    public ReservationDTO getReservation(@PathVariable("id") Long id) {
        return reservationService.getReservationById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
    }
}
