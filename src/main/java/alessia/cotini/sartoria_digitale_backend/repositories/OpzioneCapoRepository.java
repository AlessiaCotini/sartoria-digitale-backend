package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.OpzioneCapo;
import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpzioneCapoRepository extends JpaRepository<OpzioneCapo, UUID> {
    List<OpzioneCapo> findByCategorieApplicabiliContaining(CategoriaCapo categoria);
}
