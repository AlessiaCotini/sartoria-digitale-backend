package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Pagamento;
import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import alessia.cotini.sartoria_digitale_backend.payloads.PagamentoResponse;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistraAccontoRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistraSaldoRequest;
import alessia.cotini.sartoria_digitale_backend.services.OrdineService;
import alessia.cotini.sartoria_digitale_backend.services.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagamenti")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final OrdineService ordineService;

    public PagamentoController(PagamentoService pagamentoService, OrdineService ordineService) {
        this.pagamentoService = pagamentoService;
        this.ordineService = ordineService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO', 'SUPER_ADMIN')")
    public List<PagamentoResponse> tutti() {
        return pagamentoService.trovaTutti().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/ordine/{ordineId}")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO', 'SUPER_ADMIN')")
    public PagamentoResponse perOrdine(@PathVariable UUID ordineId) {
        return toResponse(pagamentoService.trovaPerOrdine(ordineId));
    }

    @PatchMapping("/ordine/{ordineId}/acconto")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public PagamentoResponse registraAcconto(@PathVariable UUID ordineId,
                                             @RequestBody @Valid RegistraAccontoRequest request) {
        return toResponse(pagamentoService.registraAcconto(ordineId, request));
    }

    @PatchMapping("/ordine/{ordineId}/saldo")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public PagamentoResponse registraSaldo(@PathVariable UUID ordineId,
                                           @RequestBody @Valid RegistraSaldoRequest request) {
        PagamentoResponse response = toResponse(pagamentoService.registraSaldo(ordineId, request));
        ordineService.cambiaStato(ordineId, StatoOrdine.COMPLETATO);
        return response;
    }


    private PagamentoResponse toResponse(Pagamento p) {
        return new PagamentoResponse(
                p.getId(),
                p.getOrdine().getId(),
                p.getAccontoImporto(),
                p.getAccontoData(),
                p.getAccontoMetodo(),
                p.getSaldoImporto(),
                p.getSaldoData(),
                p.getSaldoMetodo(),
                pagamentoService.statoDi(p)
        );
    }

}
