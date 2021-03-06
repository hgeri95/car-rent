package hu.bme.carrent.dto;

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

    private Long ownerId;

    public CarDTO() {
    }

    public CarDTO(Long id, @NotBlank String type, @NotBlank String city, @NotNull int capacity, Long ownerId) {
        this.id = id;
        this.type = type;
        this.city = city;
        this.capacity = capacity;
        this.ownerId = ownerId;
    }
}
