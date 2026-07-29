package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.StatoAppuntamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppuntamentoResponse(
        UUID id,
        boolean clienteRegistrato,
        UUID clienteId,
        UUID clienteNegozioId,
        String nomeCliente,
        UUID sartaId,
        String nomeSarta,
        LocalDateTime dataOra,
        LocalDateTime dataOraFine,
        StatoAppuntamento stato,
        String note
) {}
