package hu.bme.carrent.controller;

import hu.bme.carrent.dto.ReservationDTO;
import hu.bme.carrent.dto.ReservationRequest;
import hu.bme.carrent.service.CarRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    @Autowired
    private CarRentService carRentService;

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationRequest reservationRequest) {
        return carRentService.createReservation(reservationRequest);
    }

    @GetMapping(path = "/owned/{userid}")
    public List<ReservationDTO> getOwnedReservations(@PathParam("userid") Long userid) {
        return carRentService.getOwnedReservations(userid);
    }

    @GetMapping(path = "/cars/{userid}")
    public List<ReservationDTO> getReservationsForOwnedCars(@PathParam("userid") Long userid) {
        return carRentService.reservationsForOwnedCars(userid);
    }

    @GetMapping(path = "/{id}")
    public ReservationDTO getReservation(@PathParam("id") Long id) {
        return carRentService.getReservationById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteReservation(@PathParam("id") Long id) {
        carRentService.deleteReservation(id);
    }
}
