package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.StatoAppuntamento;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ModificaAppuntamentoRequest(
        LocalDateTime dataOra,
        LocalDateTime dataOraFine,
        StatoAppuntamento stato,
        String note
) {}