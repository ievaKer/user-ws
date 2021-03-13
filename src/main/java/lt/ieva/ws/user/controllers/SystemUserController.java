package lt.ieva.ws.user.controllers;

import lombok.RequiredArgsConstructor;
import lt.ieva.ws.user.controllers.beans.LoginResult;
import lt.ieva.ws.user.controllers.beans.LoginCredentials;
import lt.ieva.ws.user.controllers.beans.PublicUser;
import lt.ieva.ws.user.controllers.beans.UpdateUser;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
import lt.ieva.ws.user.database.services.SystemUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SystemUserController {
    private static final Logger log = LogManager.getLogger();
    private final SystemUserRepository userRepository;
    private final SystemUserService userService;

    // TODO: handle errors
    @GetMapping("/")
    public String home() {
        return "Welcome!";
    }

    /**
     * Endpoint for creating system users.
     * @param user to add to the repository
     * @param errors -
     * @return response entity with the user information (HTTP status 201)
     */
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody SystemUser user, Errors errors) {
        // Check if received user is valid
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing data");
        }

        log.info("Create user: {}", user);
        try {
            SystemUser saved = userRepository.saveAndFlush(user);
            return new ResponseEntity<>(PublicUser.from(saved), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this username / email already exists.", e);
        } catch (Exception e) {
            log.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to get user by id.
     * @param id -
     * @return public user instance
     */
    @GetMapping("/user/{id}")
    public PublicUser getUser(@PathVariable Long id) {
        log.info("Requesting user with id {}", id);
        Optional<SystemUser> user = userRepository.findById(id);

        if (user.isPresent()) {
            return PublicUser.from(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to get user by username.
     * @param username -
     * @return public user instance
     */
    @GetMapping("/user/username={username}")
    public PublicUser getUserByUsername(@PathVariable String username) {
        log.info("Requesting user with username {}", username);
        Optional<SystemUser> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return PublicUser.from(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to get user by email.
     * @param email -
     * @return public user instance
     */
    @GetMapping("/user/email={email}")
    public PublicUser getUserByEmail(@PathVariable String email) {
        log.info("Requesting user with email {}", email);
        Optional<SystemUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return PublicUser.from(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to get all registered users.
     * @return list of public users
     */
    @GetMapping("/users")
    public List<PublicUser> getAllUsers() {
        return userService.getAllPublic();
    }

    /**
     * Endpoint for user authentication.
     * @param credentials for user login
     * @param errors -
     * @return response entity with login operation result (HTTP status 200)
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginCredentials credentials, Errors errors) {
        // Check if received user is valid
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing data");
        }

        log.info("Request to login user: {}", credentials.getIdentifier());
        boolean result = userService.authenticateUser(credentials);
        return new ResponseEntity<>(new LoginResult(result), HttpStatus.OK);
    }

    /**
     * Endpoint to delete user by id.
     * @param id -
     * @return empty response entity
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("Delete user with id {}", id);

        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to update user data.
     * @param user data that will be uploaded to database
     * @param id user id
     * @return public user data of updated user entity (HTTP status 200)
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser user, @PathVariable Long id) {
        if (user.getId() != null && !id.equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified user ids do not match");
        }

        SystemUser updated;
        try {
            updated = userService.updateUserById(id, user);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unable to update instance due to constraint violation", e);
        }

        return new ResponseEntity<>(PublicUser.from(updated), HttpStatus.OK);
    }

    /**
     * Endpoint to update user data.
     * @param user data that will be uploaded to database (mandatory ID field)
     * @return public user data of updated user entity (HTTP status 200)
     */
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser user) {
        SystemUser updated;
        try {
            updated = userService.updateUser(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unable to update instance due to constraint violation", e);
        }

        return new ResponseEntity<>(PublicUser.from(updated), HttpStatus.OK);
    }
}