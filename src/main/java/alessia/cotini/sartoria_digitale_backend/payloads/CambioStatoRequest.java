package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import jakarta.validation.constraints.NotNull;

public record CambioStatoRequest(@NotNull StatoOrdine stato) {}
