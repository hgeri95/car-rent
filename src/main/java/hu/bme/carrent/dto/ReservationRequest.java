package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReservationRequest implements Serializable {
    private Long carId;

    private Long userId;

    @NotNull
    private Date start;

    @NotNull
    private Date end;
}
