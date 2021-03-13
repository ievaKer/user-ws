package lt.ieva.ws.user.database.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * System user data model.
 */
@Entity
@Getter
@Setter
public class SystemUser {
    /**
     * Generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @CreationTimestamp
    private Date joinedOn;

    /**
     * Required
     */
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull
    @Column(nullable = false)
    private String password;

    /**
     * Optional
     */
    private Integer age;
    private String location;
    private String name;
    private String surname;
    private String phoneNumber;

    @Override
    public String toString() {
        return "SystemUser{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", joinedOn=" + joinedOn +
                ", age=" + age +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}