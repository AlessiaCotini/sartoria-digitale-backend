package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.payloads.MessaggioResponse;
import alessia.cotini.sartoria_digitale_backend.services.MessaggioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/messaggi")
public class MessaggioController {

    private final MessaggioService messaggioService;

    public MessaggioController(MessaggioService messaggioService) {
        this.messaggioService = messaggioService;
    }

    @GetMapping("/ordine/{ordineId}")
    @PreAuthorize("isAuthenticated()")
    public List<MessaggioResponse> storico(@PathVariable UUID ordineId) {
        return messaggioService.storico(ordineId).stream()
                .map(messaggioService::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/non-letti/conteggio")
    @PreAuthorize("isAuthenticated()")
    public long conteggioNonLetti(@AuthenticationPrincipal Utente autenticato) {
        return messaggioService.contaNonLetti(autenticato.getId());
    }

    @PatchMapping("/ordine/{ordineId}/letti")
    @PreAuthorize("isAuthenticated()")
    public void segnaComeLetti(@PathVariable UUID ordineId, @AuthenticationPrincipal Utente autenticato) {
        messaggioService.segnaComeLetti(ordineId, autenticato.getId());
    }
}
