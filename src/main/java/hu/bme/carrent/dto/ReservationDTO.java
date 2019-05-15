package hu.bme.carrent.dto;

import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReservationDTO implements Serializable {

    private Long id;

    private Car car;

    private User user;

    @NotNull
    private Date start;

    @NotNull
    private Date end;
}
