package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class CarDetailDTO implements Serializable {

    private Long id;

    @NotBlank
    private String type;

    @NotBlank
    private String city;

    @NotNull
    private int capacity;

    private ReducedUserDTO owner;

    public CarDetailDTO() {
    }

    public CarDetailDTO(Long id, @NotBlank String type, @NotBlank String city, @NotNull int capacity, ReducedUserDTO owner) {
        this.id = id;
        this.type = type;
        this.city = city;
        this.capacity = capacity;
        this.owner = owner;
    }
}
