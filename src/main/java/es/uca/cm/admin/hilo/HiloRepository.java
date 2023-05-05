package es.uca.cm.admin.hilo;


import es.uca.cm.admin.username.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HiloRepository extends JpaRepository<Hilo, UUID> {
    List<Hilo> findByAutorAndHiloPadreIsNullOrderByDateCreation(Username userApp);

    List<Hilo> findByHiloPadreIsNullOrderByDateCreation();

    List<Hilo> findByMensajeContainingIgnoreCase(String mensaje);

    List<Hilo> findByAutorOrderByDateCreation(Username userApp);

    Optional<Hilo> findById(UUID id);

    List<Hilo> findByHiloPadreOrderByDateCreation(Hilo hiloPadre);



}

