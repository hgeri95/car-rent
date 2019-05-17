package hu.bme.carrent.controller.ajax;

import hu.bme.carrent.dto.CarDTO;
import hu.bme.carrent.dto.SearchRequest;
import hu.bme.carrent.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    @Autowired
    private CarService carService;

    @PostMapping("/ajax/car/search")
    public ResponseEntity<?> searchCars(@Valid @RequestBody SearchRequest searchRequest, Errors errors) {
        AjaxResponseBody result = new AjaxResponseBody();

        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors()
                    .stream().map(ex -> ex.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);
        }

        List<CarDTO> cars = carService.searchFreeCarsByCityByStartByEnd(
                searchRequest.getCity(), searchRequest.getStart(), searchRequest.getEnd());
        result.setResult(cars);

        return ResponseEntity.ok(result);
    }
}
