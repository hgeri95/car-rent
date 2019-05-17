package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.UserDTO;
import hu.bme.carrent.service.UserService;
import hu.bme.carrent.dto.UserFormDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class UserTHController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/adduser")
    public String addUser(@Valid @ModelAttribute("user") UserFormDTO user) {
        log.info("Add user: {}", user);
        userService.createUser(new UserDTO(user.getUsername(), user.getPassword(), user.getRoles()));

        return "index";
    }
}
