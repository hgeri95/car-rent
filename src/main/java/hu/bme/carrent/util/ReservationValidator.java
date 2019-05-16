package hu.bme.carrent.util;

import hu.bme.carrent.model.Reservation;

import java.util.Date;
import java.util.List;

public class ReservationValidator {

    public static boolean isReservationAcceptable(Date newStart, Date newEnd, List<Reservation> oldReservations) {
        for (Reservation reservation : oldReservations) {
            if (!isReservationAcceptable(newStart, newEnd, reservation)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isReservationAcceptable(Date start, Date end, Reservation reservation) {
        if (start.after(reservation.getStart()) && start.before(reservation.getEnd())) {
            return false;
        }
        if (end.after(reservation.getStart()) && end.before(reservation.getEnd())) {
            return false;
        }
        if (start.before(reservation.getStart()) && end.after(reservation.getEnd())) {
            return false;
        }
        return true;
    }
}
