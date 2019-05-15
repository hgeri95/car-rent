package hu.bme.carrent.dto;

import hu.bme.carrent.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class CarDTO implements Serializable {

    private Long id;

    @NotBlank
    private String type;

    @NotBlank
    private String city;

    @NotNull
    private int capacity;

    private User owner;
}
