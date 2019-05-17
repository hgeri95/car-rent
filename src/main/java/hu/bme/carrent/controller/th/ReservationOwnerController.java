package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.ReservationDTO;
import hu.bme.carrent.model.User;
import hu.bme.carrent.security.SecurityServiceImpl;
import hu.bme.carrent.service.ReservationService;
import hu.bme.carrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReservationOwnerController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private UserService userService;

    @GetMapping("/ownerpage/reservations")
    public String getRelatedReservations(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userService.findUserByName(username);

        List<ReservationDTO> reservations = reservationService.reservationsForOwnedCars(user.getId());
        model.addAttribute("reservations", reservations);
        return "owner/reservations";
    }
}
