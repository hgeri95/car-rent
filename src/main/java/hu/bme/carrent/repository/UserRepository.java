package hu.bme.carrent.repository;

import hu.bme.carrent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public int deleteUserByUsername(String username);
    public User findUserByUsername(String username);
    public User findUserByCarsId(Long id);
}
