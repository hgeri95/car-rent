package hu.bme.carrent.dto;

import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO implements Serializable {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<Role> roles = new HashSet<>();

    private Set<Car> cars = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(@NotBlank String username, @NotBlank String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
