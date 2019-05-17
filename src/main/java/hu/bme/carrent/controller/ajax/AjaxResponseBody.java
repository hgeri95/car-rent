package hu.bme.carrent.controller.ajax;

import hu.bme.carrent.dto.CarDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AjaxResponseBody {

    String msg;
    List<CarDTO> result;
}
