package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CarFormDTO {
    @NotBlank
    private String type;

    @NotBlank
    private String city;

    @NotNull
    private int capacity;
}
