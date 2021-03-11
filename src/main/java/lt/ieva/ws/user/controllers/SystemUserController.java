package lt.ieva.ws.user.controllers;

import lombok.RequiredArgsConstructor;
import lt.ieva.ws.user.controllers.beans.LoginUser;
import lt.ieva.ws.user.controllers.beans.PublicUser;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
import lt.ieva.ws.user.database.services.SystemUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user")
    public PublicUser createUser(@RequestBody SystemUser user) {
        log.info("Create user: {}", user);
        return PublicUser.from(userRepository.save(user));
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

    @GetMapping("/users")
    public List<PublicUser> getAllUsers() {
        return userService.getAllPublic();
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestBody LoginUser user) {
        return userService.authenticateUser(user);
    }

    // TODO: needs implementation
    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable String id) {
        return false;
    }

    // TODO: needs implementation
    @PutMapping("/user")
    public boolean updateUser() {
        return false;
    }
}