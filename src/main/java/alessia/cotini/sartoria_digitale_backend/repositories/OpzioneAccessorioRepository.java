package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.OpzioneAccessorio;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpzioneAccessorioRepository extends JpaRepository<OpzioneAccessorio, UUID> {
    List<OpzioneAccessorio> findByTipiApplicabiliContaining(TipoAccessorio tipo);
}
