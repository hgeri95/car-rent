package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SearchRequest implements Serializable {

    @NotNull
    private String city;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;
}
