package lt.ieva.ws.user.controllers.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A bean analogue of SystemUser entity with fields available for update.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateUser {
    public Long id;
    public String username;
    public String email;
    public String password;
    public Integer age;
    public String location;
    public String name;
    public String surname;
    public String phoneNumber;
}
