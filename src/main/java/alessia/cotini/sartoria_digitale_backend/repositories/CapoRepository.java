package alessia.cotini.sartoria_digitale_backend.repositories;

import alessia.cotini.sartoria_digitale_backend.entities.Capo;
import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CapoRepository extends JpaRepository<Capo, UUID> {
    List<Capo> findByGenere(Genere genere);
    List<Capo> findByCategoria(CategoriaCapo categoria);
    List<Capo> findByGenereAndCategoria(Genere genere, CategoriaCapo categoria);
}
