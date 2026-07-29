package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreazioneCapoRequest(
        @NotBlank String nome,
        @NotNull Genere genere,
        @NotNull CategoriaCapo categoria,
        String modello,
        String tessuto,
        @NotNull @Positive Double prezzoDa,
        boolean inEvidenza,
        String immagine
) {}
