package alessia.cotini.sartoria_digitale_backend.repositories;


import alessia.cotini.sartoria_digitale_backend.entities.Accessorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessorioRepository extends JpaRepository<Accessorio, UUID> {
}