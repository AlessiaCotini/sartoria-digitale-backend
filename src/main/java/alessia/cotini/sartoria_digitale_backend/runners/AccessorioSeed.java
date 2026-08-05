package alessia.cotini.sartoria_digitale_backend.runners;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;

public record AccessorioSeed(
        String nome, Genere genere, TipoAccessorio tipo, String modello,
        String tessuto, Double prezzoDa, boolean inEvidenza
) {}
