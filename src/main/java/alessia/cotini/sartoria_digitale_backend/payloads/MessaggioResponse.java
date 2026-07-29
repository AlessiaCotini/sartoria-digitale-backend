package alessia.cotini.sartoria_digitale_backend.payloads;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessaggioResponse(
        UUID id,
        UUID ordineId,
        UUID mittenteId,
        String nomeMittente,
        String testo,
        LocalDateTime dataInvio
) {}