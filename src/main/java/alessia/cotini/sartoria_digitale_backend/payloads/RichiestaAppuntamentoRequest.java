package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RichiestaAppuntamentoRequest(
        @NotNull LocalDateTime dataOra,
        @NotNull LocalDateTime dataOraFine,
        String note
) {}
