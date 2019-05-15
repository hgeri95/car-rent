package hu.bme.carrent.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SearchRequest implements Serializable {

    private String city;
    private Date start;
    private Date end;
}
