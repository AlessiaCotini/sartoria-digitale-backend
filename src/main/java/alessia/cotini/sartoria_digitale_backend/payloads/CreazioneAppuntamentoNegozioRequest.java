package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreazioneAppuntamentoNegozioRequest(
        UUID clienteId,
        UUID clienteNegozioId,
        String nomeClienteNuovo,
        String cognomeClienteNuovo,
        String telefonoClienteNuovo,
        @NotNull LocalDateTime dataOra,
        @NotNull LocalDateTime dataOraFine,
        String note
) {}