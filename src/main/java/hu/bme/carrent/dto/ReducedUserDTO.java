package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class ReducedUserDTO implements Serializable {
    private Long id;

    @NotBlank
    private String username;

    public ReducedUserDTO() {
    }

    public ReducedUserDTO(Long id, @NotBlank String username) {
        this.id = id;
        this.username = username;
    }
}
