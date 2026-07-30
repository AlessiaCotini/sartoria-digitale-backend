package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Ordine;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.payloads.*;
import alessia.cotini.sartoria_digitale_backend.services.OrdineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordini")
public class OrdineController {

    private final OrdineService ordineService;

    public OrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public OrdineResponse crea(@RequestBody @Valid CreazioneOrdineRequest request,
                               @AuthenticationPrincipal Utente autenticato) {
        return toResponse(ordineService.creaOrdine(request, autenticato));
    }

    @GetMapping("/miei")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<OrdineResponse> mieiOrdini(@AuthenticationPrincipal Utente autenticato) {
        return ordineService.trovaDelCliente(autenticato.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/assegnati")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public List<OrdineResponse> assegnatiAme(@AuthenticationPrincipal Utente autenticato) {
        return ordineService.trovaAssegnati(autenticato.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SARTA', 'SUPER_ADMIN')")
    public List<OrdineResponse> tutti() {
        return ordineService.trovaTutti().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{id}/assegna")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public OrdineResponse assegnaAme(@PathVariable UUID id, @AuthenticationPrincipal Utente autenticato) {
        return toResponse(ordineService.assegna(id, autenticato));
    }

    @PatchMapping("/{id}/stato")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO', 'SUPER_ADMIN')")
    public OrdineResponse cambiaStato(@PathVariable UUID id, @RequestBody @Valid CambioStatoRequest request) {
        return toResponse(ordineService.cambiaStato(id, request.stato()));
    }

    private OrdineResponse toResponse(Ordine ordine) {
        boolean registrato = ordine.getCliente() != null;
        List<OpzioneResponse> opzioni = ordine.getOpzioniScelte().stream()
                .map(o -> new OpzioneResponse(o.getId(), o.getNome(), o.getTipo(), o.getSovrapprezzo()))
                .collect(Collectors.toList());

        return new OrdineResponse(
                ordine.getId(),
                registrato,
                registrato ? ordine.getCliente().getId() : null,
                registrato ? null : ordine.getClienteNegozio().getId(),
                registrato
                        ? ordine.getCliente().getNome() + " " + ordine.getCliente().getCognome()
                        : ordine.getClienteNegozio().getNome() + " " + ordine.getClienteNegozio().getCognome(),
                registrato ? null : ordine.getClienteNegozio().getTelefono(),
                ordine.getCapo().getId(),
                ordine.getCapo().getNome(),
                ordine.getMateriale().getId(),
                ordine.getMateriale().getNome(),
                ordine.getColore(),
                opzioni,
                ordine.getStato(),
                ordine.getAssegnatoA() != null ? ordine.getAssegnatoA().getId() : null,
                ordine.getPrezzoTotale(),
                ordine.getDataCreazione()
        );
    }

    @PostMapping("/negozio")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public OrdineResponse creaPerClienteNegozio(@RequestBody @Valid CreazioneOrdineNegozioRequest request,
                                                @AuthenticationPrincipal Utente autenticato) {
        return toResponse(ordineService.creaOrdineNegozio(request, autenticato));
    }

    @PatchMapping("/{id}/prezzo")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public OrdineResponse modificaPrezzo(@PathVariable UUID id, @RequestBody @Valid ModificaPrezzoRequest request) {
        return toResponse(ordineService.modificaPrezzo(id, request.prezzoTotale()));
    }

    @GetMapping("/magazzino")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO', 'SUPER_ADMIN')")
    public List<OrdineResponse> codaMagazzino() {
        return ordineService.trovaCodaMagazzino().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{id}/fornitore")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO', 'SUPER_ADMIN')")
    public OrdineResponse modificaFornitore(@PathVariable UUID id, @RequestBody @Valid ModificaFornitoreRequest request) {
        return toResponse(ordineService.modificaFornitore(id, request.fornitore()));
    }
}