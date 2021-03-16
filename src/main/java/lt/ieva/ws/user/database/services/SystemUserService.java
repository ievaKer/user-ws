package lt.ieva.ws.user.database.services;

import lombok.AllArgsConstructor;
import lt.ieva.ws.user.controllers.beans.LoginCredentials;
import lt.ieva.ws.user.controllers.beans.PublicUser;
import lt.ieva.ws.user.controllers.beans.UpdateUser;
import lt.ieva.ws.user.database.models.SystemUser;
import lt.ieva.ws.user.database.repositories.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class for managing processes associated with system users.
 */
@AllArgsConstructor
@Service
public class SystemUserService {
    private final SystemUserRepository userRepository;

    /**
     * Authenticates user login credentials
     * @param user to authenticate
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(LoginCredentials user) {
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

    /**
     * Updates user data.
     * @param update user data (mandatory ID field)
     * @return updated system user instance
     */
    public SystemUser updateUser(UpdateUser update) {
        if (update.getId() == null) throw new IllegalArgumentException("No ID specified");
        return updateUserById(update.getId(), update);
    }

    /**
     * Updates user data by id.
     * @param id of user to update
     * @param update user data
     * @return updated system user instance
     */
    public SystemUser updateUserById(Long id, UpdateUser update) {
        Optional<SystemUser> optUser = userRepository.findById(id);
        SystemUser user = optUser.orElseThrow(NoSuchElementException::new);

        Method method;
        String methodName;
        String field;
        for (Field f: UpdateUser.class.getDeclaredFields()) {
            try {
                if (f.get(update) == null) continue;

                field = f.getName();
                methodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
                method = user.getClass().getMethod(methodName, f.getType());
                method.invoke(user, f.get(update));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException("Error while mapping UpdateUser to SystemUser", e);
            }
        }

        return userRepository.save(user);
    }
}
