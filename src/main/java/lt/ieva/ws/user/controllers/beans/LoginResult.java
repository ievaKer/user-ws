package lt.ieva.ws.user.controllers.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A bean for wrapping login operation result.
 */
@Getter
@AllArgsConstructor
public class LoginResult {
    private final boolean isSuccess;
}
