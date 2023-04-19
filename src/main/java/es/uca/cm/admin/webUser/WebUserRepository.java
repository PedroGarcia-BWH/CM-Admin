package es.uca.cm.admin.webUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface  WebUserRepository extends JpaRepository<WebUser, UUID> {
    WebUser findByEmail (String email);

    List<WebUser> findByDateDeletedIsNull();
}
