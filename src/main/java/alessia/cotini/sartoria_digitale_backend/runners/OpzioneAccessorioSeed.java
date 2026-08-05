package alessia.cotini.sartoria_digitale_backend.runners;

import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import alessia.cotini.sartoria_digitale_backend.enums.TipoOpzione;

import java.util.List;

public record OpzioneAccessorioSeed(String nome, TipoOpzione tipo, Double sovrapprezzo, List<TipoAccessorio> tipiApplicabili) {}
