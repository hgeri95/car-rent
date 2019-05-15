package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class CarRequest implements Serializable {
    @NotBlank
    private String type;

    @NotBlank
    private String city;

    @NotNull
    private int capacity;

    private Long ownerId;
}
