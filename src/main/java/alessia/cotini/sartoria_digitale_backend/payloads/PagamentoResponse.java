package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.MetodoPagamento;
import alessia.cotini.sartoria_digitale_backend.enums.StatoPagamento;

import java.time.LocalDate;
import java.util.UUID;

public record PagamentoResponse(
        UUID id,
        UUID ordineId,
        Double accontoImporto,
        LocalDate accontoData,
        MetodoPagamento accontoMetodo,
        Double saldoImporto,
        LocalDate saldoData,
        MetodoPagamento saldoMetodo,
        StatoPagamento stato
) {}
