package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Misure;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.payloads.MisureRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.MisureRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/misure")
public class MisureController {

    private final MisureRepository misureRepository;

    public MisureController(MisureRepository misureRepository) {
        this.misureRepository = misureRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public MisureRequest mie(@AuthenticationPrincipal Utente autenticato) {
        Misure m = misureRepository.findByUtenteId(autenticato.getId())
                .orElseThrow(() -> new NotFoundException("Misure non trovate"));
        return new MisureRequest(m.getAltezza(), m.getPeso(), m.getTorace(), m.getVita(), m.getFianchi(),
                m.getSpalle(), m.getManica(), m.getGamba(), m.getCollo(), m.getBicipite(), m.getPolso(),
                m.getBusto(), m.getCoscia(), m.getGinocchio(), m.getCaviglia());
    }
    @PutMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public MisureRequest aggiorna(@AuthenticationPrincipal Utente autenticato, @RequestBody MisureRequest request) {
        Misure m = misureRepository.findByUtenteId(autenticato.getId())
                .orElseThrow(() -> new NotFoundException("Misure non trovate"));

        m.setAltezza(request.altezza());
        m.setPeso(request.peso());
        m.setTorace(request.torace());
        m.setVita(request.vita());
        m.setFianchi(request.fianchi());
        m.setSpalle(request.spalle());
        m.setManica(request.manica());
        m.setGamba(request.gamba());
        m.setCollo(request.collo());
        m.setBicipite(request.bicipite());
        m.setPolso(request.polso());
        m.setBusto(request.busto());
        m.setCoscia(request.coscia());
        m.setGinocchio(request.ginocchio());
        m.setCaviglia(request.caviglia());

        misureRepository.save(m);
        return request;
    }

}
