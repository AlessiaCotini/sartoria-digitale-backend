package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ModificaPrezzoRequest(@NotNull @Positive Double prezzoTotale) {}
