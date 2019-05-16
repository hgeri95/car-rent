package hu.bme.carrent;

import hu.bme.carrent.model.Car;
import hu.bme.carrent.model.Reservation;
import hu.bme.carrent.model.Role;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.CarRepository;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarRepositoryIntegrationTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void carCrudTest() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.OWNER);
        User owner = new User("username", "pwd", roles);
        userRepository.saveAndFlush(owner);

        assertEquals(owner, userRepository.findAll().get(0));

        Car car = new Car("type", "Bud", 4, owner);
        Car car2 = new Car("t", "Vien", 5, owner);
        User user = userRepository.findAll().get(0);
        user.getCars().add(car);
        user.getCars().add(car2);
        userRepository.saveAndFlush(user);

        assertEquals(2, userRepository.findAll().get(0).getCars().size());
        assertEquals(2, carRepository.findAll().size());

        user = userRepository.findAll().get(0);
        Set<Car> cars = new HashSet<>();
        cars.add(car);
        user.setCars(cars);
        userRepository.saveAndFlush(user);
        assertEquals(1, userRepository.findAll().get(0).getCars().size());
        assertEquals(1, carRepository.findAll().size());

        userRepository.deleteAll();
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, carRepository.findAll().size());
    }

    @Test
    public void reservationCrudTest() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.OWNER);
        User owner = new User("u", "pwd", roles);
        Car car = new Car("t", "Bu", 4, owner);
        owner.getCars().add(car);

        userRepository.saveAndFlush(owner);

        User customer = new User("c", "pwd", roles);
        userRepository.saveAndFlush(customer);
        Reservation reservation = new Reservation(car, customer, new Date(), new Date());
        reservationRepository.saveAndFlush(reservation);

        assertEquals(1, reservationRepository.findAll().size());

        User customer2 = new User("c2", "pwd", roles);
        userRepository.saveAndFlush(customer2);
        Reservation reservation2 = new Reservation(car, customer2, new Date(), new Date());
        reservationRepository.saveAndFlush(reservation2);

        assertEquals(2, reservationRepository.findAll().size());

        reservationRepository.deleteAll();
        assertEquals(0, reservationRepository.findAll().size());
    }
}
