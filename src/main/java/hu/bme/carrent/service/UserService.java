package hu.bme.carrent.service;

import hu.bme.carrent.dto.UserDTO;
import hu.bme.carrent.model.Role;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import hu.bme.carrent.util.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.USER);
        Set<Role> ownerRoles = new HashSet<>();
        ownerRoles.add(Role.OWNER);
        User user = new User("user", passwordEncoder.encode("12345"), userRoles);
        User owner = new User("owner", passwordEncoder.encode("12345"), ownerRoles);
        if (userRepository.findUserByUsername("user") == null) {
            userRepository.save(user);
        }
        if(userRepository.findUserByUsername("owner") == null) {
            userRepository.save(owner);
        }
        log.info("Default users created");
    }

    public User findUserByName(String username) {
        return userRepository.findUserByUsername(username);
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findUserByUsername(userDTO.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        } else {
            User user = converter.convertToEntity(userDTO);
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodedPassword);
            return converter.convertToDTO(userRepository.save(user));
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if (!userRepository.findById(userDTO.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists");
        } else {
            User user = converter.convertToEntity(userDTO);
            return converter.convertToDTO(userRepository.save(user));
        }
    }

    @Transactional
    public void deleteUserById(Long id) {
        reservationRepository.deleteAllByUserId(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists"));

        userRepository.delete(user);
    }
}
