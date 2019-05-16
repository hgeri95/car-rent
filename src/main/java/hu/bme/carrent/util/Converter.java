package hu.bme.carrent.util;

import hu.bme.carrent.dto.*;
import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Reservation;
import hu.bme.carrent.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Converter {

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public ReservationDTO convertToDTO(Reservation reservation) {
        Car car = reservation.getCar();
        return new ReservationDTO(reservation.getId(),
                new CarDTO(car.getId(), car.getType(), car.getCity(), car.getCapacity(), car.getOwner().getId()),
                new ReducedUserDTO(reservation.getUser().getId(), reservation.getUser().getUsername()),
                reservation.getStart(), reservation.getEnd());
    }

    public Car convertToEntity(CarDTO carDTO, User owner) {
        return new Car(carDTO.getType(), carDTO.getCity(), carDTO.getCapacity(), owner);
    }

    public Car convertToEntity(CarRequest carRequest, User owner) {
        return new Car(carRequest.getType(), carRequest.getCity(), carRequest.getCapacity(), owner);
    }

    public CarDTO convertToDTO(Car car) {
        return new CarDTO(car.getId(), car.getType(), car.getCity(), car.getCapacity(), car.getOwner().getId());
    }
}
