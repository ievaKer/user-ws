package lt.ieva.ws.user.controllers.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * A bean for user authentication.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {
    @NotNull
    private String password;
    @NotNull
    private String identifier;
}
