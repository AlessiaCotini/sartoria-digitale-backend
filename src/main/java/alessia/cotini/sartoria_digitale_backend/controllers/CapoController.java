package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Capo;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneCapoRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.CapoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capi")
public class CapoController {

    private final CapoRepository capoRepository;

    public CapoController(CapoRepository capoRepository) {
        this.capoRepository = capoRepository;
    }

    @GetMapping
    public List<Capo> tutti() {
        return capoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SARTA', 'SUPER_ADMIN')")
    public Capo crea(@RequestBody @Valid CreazioneCapoRequest request) {
        Capo capo = new Capo();
        capo.setNome(request.nome());
        capo.setGenere(request.genere());
        capo.setCategoria(request.categoria());
        capo.setModello(request.modello());
        capo.setTessuto(request.tessuto());
        capo.setPrezzoDa(request.prezzoDa());
        capo.setInEvidenza(request.inEvidenza());
        capo.setImmagine(request.immagine());
        return capoRepository.save(capo);
    }
}
