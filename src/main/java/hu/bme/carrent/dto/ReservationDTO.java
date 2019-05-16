package hu.bme.carrent.dto;

import hu.bme.carrent.model.Car;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReservationDTO implements Serializable {

    private Long id;

    private CarDTO car;

    private ReducedUserDTO user;

    @NotNull
    private Date start;

    @NotNull
    private Date end;

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, CarDTO car, ReducedUserDTO user, @NotNull Date start, @NotNull Date end) {
        this.id = id;
        this.car = car;
        this.user = user;
        this.start = start;
        this.end = end;
    }
}
