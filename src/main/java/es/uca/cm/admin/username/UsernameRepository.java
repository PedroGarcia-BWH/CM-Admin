package es.uca.cm.admin.username;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsernameRepository extends JpaRepository<Username, UUID> {
    Optional<Username> findByUsername(String username);

    List<Username> findByUsernameContainingIgnoreCase(String username);

    Username findByFirebaseId(String firebaseId);
}

