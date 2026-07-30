package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrdineResponse(
        UUID id,
        boolean clienteRegistrato,
        UUID clienteId,
        UUID clienteNegozioId,
        String nomeCliente,
        String telefonoCliente,
        UUID capoId,
        String capoNome,
        UUID materialeId,
        String materialeNome,
        String colore,
        List<OpzioneResponse> opzioni,
        StatoOrdine stato,
        UUID assegnatoAId,
        Double prezzoTotale,
        LocalDateTime dataCreazione
) {}
