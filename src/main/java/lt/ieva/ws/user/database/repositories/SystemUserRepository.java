package lt.ieva.ws.user.database.repositories;

import lt.ieva.ws.user.database.models.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
}