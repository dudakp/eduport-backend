package sk.dudak.eduport.dao.course;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.dudak.eduport.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
