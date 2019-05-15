package hu.bme.carrent.controller;

import hu.bme.carrent.dto.CarDTO;
import hu.bme.carrent.dto.CarRequest;
import hu.bme.carrent.dto.SearchRequest;
import hu.bme.carrent.dto.UserDTO;
import hu.bme.carrent.service.CarRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("car")
public class CarController {

    @Autowired
    private CarRentService carRentService;

    @PostMapping
    public UserDTO addCarForUser(@RequestBody CarRequest car) {
        return carRentService.addCarToUser(car);
    }

    @GetMapping(path = "/all")
    public List<CarDTO> getAll() {
        return carRentService.getAllCars();
    }

    @GetMapping(path = "/owned/{userid}")
    public List<CarDTO> getOwnedCars(@PathParam("userid") Long userId) {
        return carRentService.getOwnedCars(userId);
    }

    @GetMapping(path = "/{id}")
    public CarDTO getCar(@PathParam("id") Long id) {
        return carRentService.getCarById(id);
    }

    @PostMapping(path = "/search")
    public List<CarDTO> searchCars(@RequestBody SearchRequest searchRequest) {
        return carRentService.searchFreeCarsByCityByStartByEnd(searchRequest.getCity(),
                searchRequest.getStart(), searchRequest.getEnd());
    }

    @PutMapping
    public UserDTO updateCar(@RequestBody CarDTO carDTO) {
        return carRentService.updateCar(carDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCar(@PathParam("id") Long id) {
        carRentService.deleteCar(id);
    }


}
