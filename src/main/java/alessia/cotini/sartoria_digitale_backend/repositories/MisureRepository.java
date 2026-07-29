package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Misure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MisureRepository extends JpaRepository<Misure, UUID> {
    Optional<Misure> findByUtenteId(UUID utenteId);
}
