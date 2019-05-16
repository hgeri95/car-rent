package hu.bme.carrent.controller;

import hu.bme.carrent.dto.*;
import hu.bme.carrent.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("car")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public UserDTO addCarForUser(@RequestBody CarRequest car) {
        return carService.addCarToUser(car);
    }

    @GetMapping(path = "/all")
    public List<CarDTO> getAll() {
        return carService.getAllCars();
    }

    @GetMapping(path = "/owned/{userid}")
    public List<CarDTO> getOwnedCars(@PathVariable("userid") Long userId) {
        return carService.getOwnedCars(userId);
    }

    @GetMapping(path = "/{id}")
    public CarDetailDTO getCar(@PathVariable("id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping(path = "/search")
    public List<CarDTO> searchCars(@RequestBody SearchRequest searchRequest) {
        return carService.searchFreeCarsByCityByStartByEnd(searchRequest.getCity(),
                searchRequest.getStart(), searchRequest.getEnd());
    }

    @PutMapping
    public UserDTO updateCar(@RequestBody CarDTO carDTO) {
        return carService.updateCar(carDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id);
    }


}
