package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreazioneOrdineNegozioRequest(
        @NotBlank String nomeCliente,
        @NotBlank String cognomeCliente,
        @NotBlank String telefonoCliente,
        @NotNull @Valid MisureRequest misure,
        @NotNull UUID capoId,
        @NotNull UUID materialeId,
        @NotBlank String colore
) {}
