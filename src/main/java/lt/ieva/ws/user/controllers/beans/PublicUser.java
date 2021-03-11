package lt.ieva.ws.user.controllers.beans;

import lombok.Getter;
import lt.ieva.ws.user.database.models.SystemUser;

import java.util.Date;

/**
 * A bean containing non-secret user information.
 */
@Getter
public class PublicUser {
    private Long Id;
    private String username;
    private String email;
    private Date joinedOn;
    private Integer age;
    private String location;
    private String name;
    private String surname;
    private String phoneNumber;

    private PublicUser() {}

    public static PublicUser from(SystemUser user) {
        PublicUser pUser = new PublicUser();

        pUser.username = user.getUsername();
        pUser.age = user.getAge();
        pUser.email = user.getEmail();
        pUser.Id = user.getId();
        pUser.joinedOn = user.getJoinedOn();
        pUser.location = user.getLocation();
        pUser.name = user.getName();
        pUser.surname = user.getSurname();
        pUser.phoneNumber = user.getPhoneNumber();

        return pUser;
    }
}
