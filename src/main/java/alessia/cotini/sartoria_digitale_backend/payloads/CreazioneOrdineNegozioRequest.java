package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreazioneOrdineNegozioRequest(
        UUID clienteId,
        UUID clienteNegozioId,
        String nomeClienteNuovo,
        String cognomeClienteNuovo,
        String telefonoClienteNuovo,
        @Valid MisureRequest misureClienteNuovo,
        @NotNull UUID capoId,
        @NotNull UUID materialeId,
        @NotBlank String colore,
        List<UUID> opzioniIds
) {}
