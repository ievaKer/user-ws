package lt.ieva.ws.user.database.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class SystemUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private String country; // TODO: maybe use enum instead?
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    private Date joinedOn;

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", password='" + password + '\'' +
                ", joinedOn=" + joinedOn +
                '}';
    }
}