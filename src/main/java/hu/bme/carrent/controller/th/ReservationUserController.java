package hu.bme.carrent.controller.th;

import hu.bme.carrent.dto.CarDetailDTO;
import hu.bme.carrent.dto.ReservationDTO;
import hu.bme.carrent.dto.ReservationRequest;
import hu.bme.carrent.model.User;
import hu.bme.carrent.security.SecurityServiceImpl;
import hu.bme.carrent.service.ReservationService;
import hu.bme.carrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ReservationUserController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private UserService userService;

    @GetMapping("/userpage/reservations")
    public String getReservations(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userService.findUserByName(username);

        List<ReservationDTO> reservations= reservationService.getOwnedReservations(user.getId());
        model.addAttribute("reservations", reservations);
        return "user/reservations";
    }

    @PostMapping("/userpage/reservation/{id}")
    public String reserve(@PathVariable("id") long carId,
                          @Valid @ModelAttribute("reservation") ReservationRequest reservationRequest,
                          @ModelAttribute("car") CarDetailDTO carDetailDTO,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/car-detail";
        }
        String username = securityService.findLoggedInUsername();
        User user = userService.findUserByName(username);
        ReservationRequest extendedRequest = new ReservationRequest(carId,
                user.getId(), reservationRequest.getStart(), reservationRequest.getEnd());
        reservationService.createReservation(extendedRequest);

        return "user/index";
    }

    @GetMapping("/userpage/reservation/delete/{id}")
    public String deleteReservation(@PathVariable("id") long id, Model model) {
        Long userId = reservationService.getReservationById(Long.valueOf(id)).getUser().getId();
        reservationService.deleteReservation(Long.valueOf(id));
        List<ReservationDTO> reservations = reservationService.getOwnedReservations(userId);
        model.addAttribute("reservations", reservations);
        return "/user/reservations";
    }
}
