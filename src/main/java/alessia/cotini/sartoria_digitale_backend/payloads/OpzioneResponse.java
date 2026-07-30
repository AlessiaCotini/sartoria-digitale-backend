package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.TipoOpzione;

import java.util.UUID;

public record OpzioneResponse(
        UUID id,
        String nome,
        TipoOpzione tipo,
        Double sovrapprezzo
) {}
