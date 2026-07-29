package alessia.cotini.sartoria_digitale_backend.payloads;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreazioneOrdineRequest(
        @NotNull UUID capoId,
        @NotNull UUID materialeId,
        @NotBlank String colore
) {}