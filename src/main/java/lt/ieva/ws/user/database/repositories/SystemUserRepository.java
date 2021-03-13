package lt.ieva.ws.user.database.repositories;

import lt.ieva.ws.user.database.models.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Database repository interface for managing system user data.
 */
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    /**
     * Checks whether a user with specified username and password exists
     * @param username of user
     * @param password used to authenticate user
     * @return true if user was found, false otherwise
     */
    boolean existsByUsernameAndPassword(String username, String password);

    /**
     * Checks whether a user with specified email and password exists
     * @param email of user
     * @param password used to authenticate user
     * @return true if user was found, false otherwise
     */
    boolean existsByEmailAndPassword(String email, String password);

    /**
     * Finds users by username
     * @param username of user
     * @return optional value of user
     */
    Optional<SystemUser> findByUsername(String username);

    /**
     * Finds user by email
     * @param email of user
     * @return optional value of user
     */
    Optional<SystemUser> findByEmail(String email);
}