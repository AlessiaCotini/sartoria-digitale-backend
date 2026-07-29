package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.StatoAppuntamento;

import java.time.LocalDateTime;

public record ModificaAppuntamentoRequest(
        LocalDateTime dataOra,
        StatoAppuntamento stato,
        String note
) {}