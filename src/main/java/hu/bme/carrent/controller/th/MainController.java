package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.UserFormDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/userpage")
    public String userIndex() { return "user/index"; }

    @GetMapping("/ownerpage")
    public String ownerIndex() {
         return "owner/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserFormDTO());
        return "registration";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }
}
