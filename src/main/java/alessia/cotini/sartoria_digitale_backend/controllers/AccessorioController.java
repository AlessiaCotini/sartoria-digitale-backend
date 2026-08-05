package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Accessorio;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneAccessorioRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.AccessorioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accessori")
public class AccessorioController {

    private final AccessorioRepository accessorioRepository;

    public AccessorioController(AccessorioRepository accessorioRepository) {
        this.accessorioRepository = accessorioRepository;
    }

    @GetMapping
    public List<Accessorio> tutti() {
        return accessorioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Accessorio uno(@PathVariable UUID id) {
        return accessorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessorio non trovato con id " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SARTA', 'SUPER_ADMIN')")
    public Accessorio crea(@RequestBody @Valid CreazioneAccessorioRequest request) {
        Accessorio accessorio = new Accessorio();
        accessorio.setNome(request.nome());
        accessorio.setGenere(request.genere());
        accessorio.setTipo(request.tipo());
        accessorio.setModello(request.modello());
        accessorio.setTessuto(request.tessuto());
        accessorio.setPrezzoDa(request.prezzoDa());
        accessorio.setInEvidenza(request.inEvidenza());
        return accessorioRepository.save(accessorio);
    }
}
