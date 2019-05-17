package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.CarDTO;
import hu.bme.carrent.dto.CarDetailDTO;
import hu.bme.carrent.dto.ReservationRequest;
import hu.bme.carrent.dto.SearchRequest;
import hu.bme.carrent.security.SecurityServiceImpl;
import hu.bme.carrent.service.CarService;
import hu.bme.carrent.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class CarUserController {

    @Autowired
    private CarService carService;

    @GetMapping("/userpage/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "user/view-cars";
    }

    @GetMapping("/userpage/cardetails/{id}")
    public String userCarDetails(@PathVariable("id") long id, Model model) {
        CarDetailDTO carDetailDTO = carService.getCarById(Long.valueOf(id));
        log.info("Car detail: {}", carDetailDTO);
        model.addAttribute("car", carDetailDTO);
        model.addAttribute("reservation", new ReservationRequest());
        return "user/car-detail";
    }

    @GetMapping("userpage/search")
    public String getSearchPage(Model model) {
        model.addAttribute("cars", new ArrayList<CarDTO>());
        model.addAttribute("searchObject", new SearchRequest());
        return "user/search";
    }

    @PostMapping("userpage/search")
    public String getSearchPage(@Valid @ModelAttribute("searchObject") SearchRequest searchRequest,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "userpage/search";
        }
        List<CarDTO> searchResult = carService
                .searchFreeCarsByCityByStartByEnd(searchRequest.getCity(),
                        searchRequest.getStart(), searchRequest.getEnd());


        model.addAttribute("cars", searchResult);
        model.addAttribute("searchObject", searchRequest);
        return "user/search";
    }
}
