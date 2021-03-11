package lt.ieva.ws.user.database.services;

import lombok.AllArgsConstructor;
import lt.ieva.ws.user.controllers.beans.LoginUser;
import lt.ieva.ws.user.controllers.beans.PublicUser;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SystemUserService {
    private final SystemUserRepository userRepository;

    /**
     * Authenticates user login credentials
     * @param user to authenticate
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(LoginUser user) {
        return userRepository.existsByEmailAndPassword(user.getIdentifier(), user.getPassword()) ||
               userRepository.existsByUsernameAndPassword(user.getIdentifier(), user.getPassword());
    }

    /**
     * Get all public user data from database
     * @return list of PublicUser instances
     */
    public List<PublicUser> getAllPublic() {
        List<SystemUser> users = userRepository.findAll();
        return users.stream().map(PublicUser::from).collect(Collectors.toList());
    }
}
