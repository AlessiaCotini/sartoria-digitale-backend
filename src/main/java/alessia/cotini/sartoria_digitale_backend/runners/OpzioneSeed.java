package alessia.cotini.sartoria_digitale_backend.runners;
import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.enums.TipoOpzione;

import java.util.List;

public record OpzioneSeed(String nome, TipoOpzione tipo, Double sovrapprezzo, List<CategoriaCapo> categorieApplicabili) {}