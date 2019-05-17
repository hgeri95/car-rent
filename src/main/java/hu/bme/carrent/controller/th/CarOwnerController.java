package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.CarDTO;
import hu.bme.carrent.dto.CarDetailDTO;
import hu.bme.carrent.dto.CarRequest;
import hu.bme.carrent.model.User;
import hu.bme.carrent.security.SecurityServiceImpl;
import hu.bme.carrent.service.CarService;
import hu.bme.carrent.service.UserService;
import hu.bme.carrent.dto.CarFormDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class CarOwnerController {

    @Autowired
    private CarService carService;

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private UserService userService;

    @GetMapping("/ownerpage/car")
    public String addCar(Model model) {
        model.addAttribute("car", new CarFormDTO());
        return "owner/add-car";
    }

    @PostMapping("ownerpage/car")
    public String addCar(@Valid @ModelAttribute("car") CarFormDTO car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owner/add-car";
        }
        log.info("Add car: {}", car);
        String username = securityService.findLoggedInUsername();
        log.info("Logged in user: {}", username);
        User user = userService.findUserByName(username);
        CarRequest carRequest = new CarRequest(car.getType(), car.getCity(), car.getCapacity(), user.getId());
        carService.addCarToUser(carRequest);

        return "owner/index";
    }

    @GetMapping("/ownerpage/cars")
    public String getOwnedCars(Model model) {
        log.info("Get owned cars");
        String username = securityService.findLoggedInUsername();
        User user = userService.findUserByName(username);
        List<CarDTO> cars = carService.getOwnedCars(user.getId());

        model.addAttribute("cars", cars);
        return "owner/cars";
    }

    @GetMapping("/ownerpage/cardetails/{id}")
    public String carDetails(@PathVariable("id") long id, Model model) {
        CarDetailDTO carDetail = carService.getCarById(Long.valueOf(id));
        log.info("Car detail: {}", carDetail);
        model.addAttribute("car", carDetail);
        return "owner/car-detail";
    }

    @PostMapping("/ownerpage/cardetails/{id}")
    public String updateCarDetails(@PathVariable("id") long id, @Valid @ModelAttribute("car") CarDetailDTO carDetailDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owner/car-detail";
        }
        log.info("Car to update: {}", carDetailDTO);
        CarDetailDTO carDTO = carService.getCarById(carDetailDTO.getId());
        CarDTO updatedCarDTO = new CarDTO(carDetailDTO.getId(), carDetailDTO.getType(),
                carDetailDTO.getCity(), carDetailDTO.getCapacity(), carDTO.getOwner().getId());
        carService.updateCar(updatedCarDTO);
        return "owner/index";
    }

    @GetMapping("/ownerpage/car/delete/{id}")
    public String deleteCar(@PathVariable("id") long id, Model model) {
        carService.deleteCar(Long.valueOf(id));
        String username = securityService.findLoggedInUsername();
        Long userId = userService.findUserByName(username).getId();
        List<CarDTO> cars = carService.getOwnedCars(userId);
        model.addAttribute("cars", cars);
        return "owner/cars";

    }
}
