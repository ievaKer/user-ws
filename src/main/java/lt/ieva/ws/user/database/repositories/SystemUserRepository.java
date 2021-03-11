package lt.ieva.ws.user.database.repositories;

import lt.ieva.ws.user.database.models.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    boolean existsByUsernameAndPassword(String username, String password);
    boolean existsByEmailAndPassword(String email, String password);
    Optional<SystemUser> findByUsername(String username);
    Optional<SystemUser> findByEmail(String email);
}