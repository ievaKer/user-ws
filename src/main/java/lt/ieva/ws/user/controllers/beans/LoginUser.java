package lt.ieva.ws.user.controllers.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A bean for user authentication.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private String password;
    private String identifier;
}
