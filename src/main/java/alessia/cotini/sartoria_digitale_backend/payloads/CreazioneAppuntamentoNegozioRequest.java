package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreazioneAppuntamentoNegozioRequest(
        UUID clienteId,
        UUID clienteNegozioId,
        @NotNull LocalDateTime dataOra,
        String note
) {}