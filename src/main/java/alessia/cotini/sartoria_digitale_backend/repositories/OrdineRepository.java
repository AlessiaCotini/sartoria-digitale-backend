package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Ordine;
import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdineRepository extends JpaRepository<Ordine, UUID> {
    List<Ordine> findByClienteId(UUID clienteId);
    List<Ordine> findByAssegnatoAId(UUID assegnatoAId);
    List<Ordine> findByStato(StatoOrdine stato);
    List<Ordine> findByStatoIn(List<StatoOrdine> stati);
}
