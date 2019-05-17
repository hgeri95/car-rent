package hu.bme.carrent.dto;

import hu.bme.carrent.model.Role;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class UserFormDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserFormDTO() {
    }

    public UserFormDTO(@NotBlank String username, @NotBlank String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
