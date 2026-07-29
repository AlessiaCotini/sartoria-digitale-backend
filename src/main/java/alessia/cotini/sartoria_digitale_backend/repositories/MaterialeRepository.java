package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Materiale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaterialeRepository extends JpaRepository<Materiale, UUID> {
}
