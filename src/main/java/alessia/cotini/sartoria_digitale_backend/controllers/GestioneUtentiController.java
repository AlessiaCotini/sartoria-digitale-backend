package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.enums.Ruolo;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneUtenteRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.UtenteResponse;
import alessia.cotini.sartoria_digitale_backend.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utenti")
public class GestioneUtentiController {

    private final UtenteService utenteService;

    public GestioneUtentiController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/sarte")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UtenteResponse creaSarta(@RequestBody @Valid CreazioneUtenteRequest request,
                                    @AuthenticationPrincipal Utente autenticato) {
        Utente sarta = utenteService.creaConRuolo(request, Ruolo.SARTA, autenticato);
        return toResponse(sarta);
    }

    @PostMapping("/sottoposti")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SARTA')")
    public UtenteResponse creaSottoposto(@RequestBody @Valid CreazioneUtenteRequest request,
                                         @AuthenticationPrincipal Utente autenticato) {
        Utente sottoposto = utenteService.creaConRuolo(request, Ruolo.SOTTOPOSTO, autenticato);
        return toResponse(sottoposto);
    }

    private UtenteResponse toResponse(Utente utente) {
        return new UtenteResponse(utente.getId(), utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getRuolo());
    }
}
