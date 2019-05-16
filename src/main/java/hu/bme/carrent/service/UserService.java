package hu.bme.carrent.service;

import hu.bme.carrent.dto.UserDTO;
import hu.bme.carrent.model.User;
import hu.bme.carrent.repository.ReservationRepository;
import hu.bme.carrent.repository.UserRepository;
import hu.bme.carrent.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private Converter converter;

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findUserByUsername(userDTO.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        } else {
            User user = converter.convertToEntity(userDTO);
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
