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

    public CarRequest(@NotBlank String type, @NotBlank String city, @NotNull int capacity, Long ownerId) {
        this.type = type;
        this.city = city;
        this.capacity = capacity;
        this.ownerId = ownerId;
    }

    public CarRequest() {
    }
}
