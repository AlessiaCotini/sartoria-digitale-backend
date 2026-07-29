package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.payloads.MessaggioResponse;
import alessia.cotini.sartoria_digitale_backend.services.MessaggioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
}
