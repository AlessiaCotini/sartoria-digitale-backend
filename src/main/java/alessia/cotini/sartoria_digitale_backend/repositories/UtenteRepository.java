package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM Utente u WHERE u.ruolo = alessia.cotini.sartoria_digitale_backend.enums.Ruolo.CLIENTE " +
            "AND (LOWER(u.nome) LIKE LOWER(CONCAT('%', :ricerca, '%')) " +
            "OR LOWER(u.cognome) LIKE LOWER(CONCAT('%', :ricerca, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :ricerca, '%')))")
    List<Utente> cercaClienti(@Param("ricerca") String ricerca);
}
