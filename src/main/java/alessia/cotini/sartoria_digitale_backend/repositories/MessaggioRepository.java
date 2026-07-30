package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessaggioRepository extends JpaRepository<Messaggio, UUID> {
    List<Messaggio> findByOrdineIdOrderByDataInvioAsc(UUID ordineId);

    @Query("SELECT COUNT(m) FROM Messaggio m WHERE m.letto = false AND m.mittente.id <> :utenteId " +
            "AND (m.ordine.cliente.id = :utenteId OR m.ordine.assegnatoA.id = :utenteId)")
    long contaNonLettiPerUtente(@Param("utenteId") UUID utenteId);

    List<Messaggio> findByOrdineIdAndMittenteIdNotAndLettoFalse(UUID ordineId, UUID mittenteId);
}
