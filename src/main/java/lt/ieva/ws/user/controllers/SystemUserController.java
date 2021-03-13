package lt.ieva.ws.user.controllers;

import lombok.RequiredArgsConstructor;
import lt.ieva.ws.user.controllers.beans.LoginResult;
import lt.ieva.ws.user.controllers.beans.LoginCredentials;
import lt.ieva.ws.user.controllers.beans.PublicUser;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
import lt.ieva.ws.user.database.services.SystemUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping("/user/{id}")
    public PublicUser getUser(@PathVariable String id) {
        log.info("Requesting user with id {}", id);
        Long userId = Long.parseLong(id);
        Optional<SystemUser> user = userRepository.findById(userId);
        return user.map(PublicUser::from).orElse(null);
    }

    @GetMapping("/user/username={username}")
    public PublicUser getUserByUsername(@PathVariable String username) {
        log.info("Requesting user with username {}", username);
        Optional<SystemUser> user = userRepository.findByUsername(username);
        return user.map(PublicUser::from).orElse(null);
    }

    @GetMapping("/user/email={email}")
    public PublicUser getUserByEmail(@PathVariable String email) {
        log.info("Requesting user with email {}", email);
        Optional<SystemUser> user = userRepository.findByEmail(email);
        return user.map(PublicUser::from).orElse(null);
    }

    /**
     * Endpoint to get all registered users.
     * @return list of all registered public users
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

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable String id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/users")
    public void deleteUsers() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/user")
    public void updateUser() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}