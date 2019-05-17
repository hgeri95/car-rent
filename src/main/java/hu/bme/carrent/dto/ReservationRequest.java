package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReservationRequest implements Serializable {
    private Long carId;

    private Long userId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    public ReservationRequest(Long carId, Long userId, @NotNull Date start, @NotNull Date end) {
        this.carId = carId;
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public ReservationRequest() {
    }
}
