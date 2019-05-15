package hu.bme.carrent.controller;

import hu.bme.carrent.dto.UserDTO;
import hu.bme.carrent.service.CarRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private CarRentService carRentService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO user) {
        return carRentService.createUser(user);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO user) {
        return carRentService.updateUser(user);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        carRentService.deleteUserById(id);
    }
}
