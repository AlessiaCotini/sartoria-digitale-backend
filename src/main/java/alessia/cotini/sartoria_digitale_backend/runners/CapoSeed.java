package alessia.cotini.sartoria_digitale_backend.runners;
import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;

public record CapoSeed(
        String nome,
        Genere genere,
        CategoriaCapo categoria,
        String modello,
        String tessuto,
        Double prezzoDa,
        boolean inEvidenza,
        String immagine
) {}
