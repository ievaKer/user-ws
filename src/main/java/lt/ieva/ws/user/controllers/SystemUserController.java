package lt.ieva.ws.user.controllers;

import lombok.RequiredArgsConstructor;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
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

    @GetMapping("/")
    public String home() {
        return "Welcome!";
    }

    @PostMapping("/user")
    public SystemUser createUser(@RequestBody SystemUser user) {
        log.info("Received user: {}", user);
        return userRepository.save(user);
    }

    // TODO: create and always return some kind of status which in itself can have a user instance
    // TODO: create another class without passwords
    // TODO: allow to request user by email + username
    @GetMapping("/user/{id}")
    public SystemUser getUser(@PathVariable String id) {
        log.info("Requesting user with id {}", id);
        Long userId = Long.parseLong(id);
        Optional<SystemUser> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            SystemUser user = userOptional.get();
            user.setPassword(null);
            return user;
        }

        return null;
    }

    @GetMapping("/users")
    public List<SystemUser> getAllUsers() {
        List<SystemUser> users = userRepository.findAll();

        for (SystemUser user: users) {
            user.setPassword(null);
        }

        return users;
    }
}