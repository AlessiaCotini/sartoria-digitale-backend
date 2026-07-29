package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, UUID> {
    List<Appuntamento> findBySartaId(UUID sartaId);
    List<Appuntamento> findByClienteId(UUID clienteId);
}
