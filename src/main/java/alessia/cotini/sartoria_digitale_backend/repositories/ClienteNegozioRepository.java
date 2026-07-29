package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.ClienteNegozio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteNegozioRepository extends JpaRepository<ClienteNegozio, UUID> {
}
