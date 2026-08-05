package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreazioneAccessorioRequest(
        @NotBlank String nome,
        @NotNull Genere genere,
        @NotNull TipoAccessorio tipo,
        String modello,
        String tessuto,
        @NotNull @Positive Double prezzoDa,
        boolean inEvidenza
) {}
