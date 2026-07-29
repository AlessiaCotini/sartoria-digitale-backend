package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.MetodoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record RegistraAccontoRequest(
        @NotNull @Positive Double importo,
        @NotNull LocalDate data,
        @NotNull MetodoPagamento metodo
) {}
