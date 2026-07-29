package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Appuntamento;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.payloads.*;
import alessia.cotini.sartoria_digitale_backend.services.AppuntamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appuntamenti")
public class AppuntamentoController {

    private final AppuntamentoService appuntamentoService;

    public AppuntamentoController(AppuntamentoService appuntamentoService) {
        this.appuntamentoService = appuntamentoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public AppuntamentoResponse richiedi(@RequestBody @Valid RichiestaAppuntamentoRequest request,
                                         @AuthenticationPrincipal Utente autenticato) {
        return toResponse(appuntamentoService.richiedi(request, autenticato));
    }

    @PostMapping("/negozio")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public AppuntamentoResponse creaPerSarta(@RequestBody @Valid CreazioneAppuntamentoNegozioRequest request,
                                             @AuthenticationPrincipal Utente autenticato) {
        return toResponse(appuntamentoService.creaPerSarta(request, autenticato));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('SARTA', 'SOTTOPOSTO')")
    public AppuntamentoResponse modifica(@PathVariable UUID id, @RequestBody ModificaAppuntamentoRequest request,
                                         @AuthenticationPrincipal Utente autenticato) {
        return toResponse(appuntamentoService.modifica(id, request, autenticato));
    }

    @GetMapping("/miei")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<AppuntamentoResponse> mieiAppuntamenti(@AuthenticationPrincipal Utente autenticato) {
        return appuntamentoService.trovaDelCliente(autenticato.getId()).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SARTA', 'SUPER_ADMIN')")
    public List<AppuntamentoResponse> tutti() {
        return appuntamentoService.trovaTutti().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private AppuntamentoResponse toResponse(Appuntamento a) {
        boolean registrato = a.getCliente() != null;
        return new AppuntamentoResponse(
                a.getId(),
                registrato,
                registrato ? a.getCliente().getId() : null,
                registrato ? null : a.getClienteNegozio().getId(),
                registrato
                        ? a.getCliente().getNome() + " " + a.getCliente().getCognome()
                        : a.getClienteNegozio().getNome() + " " + a.getClienteNegozio().getCognome(),
                a.getSarta() != null ? a.getSarta().getId() : null,
                a.getSarta() != null ? a.getSarta().getNome() + " " + a.getSarta().getCognome() : null,
                a.getDataOra(),
                a.getDataOraFine(),
                a.getStato(),
                a.getNote()
        );
    }
}
