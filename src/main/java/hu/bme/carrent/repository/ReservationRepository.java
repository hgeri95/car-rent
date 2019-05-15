package hu.bme.carrent.repository;

import hu.bme.carrent.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation, Long> {
    public List<Reservation> findReservationsByCarId(Long id);
    public List<Reservation> findReservationsByCarCity(String city);
    public List<Reservation> findReservationsByUserId(Long id);
    public List<Reservation> findReservationsByCarOwnerId(Long id);
}
