package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Colore;
import alessia.cotini.sartoria_digitale_backend.entities.Materiale;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneMaterialeRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.MaterialeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/materiali")
public class MaterialeController {

    private final MaterialeRepository materialeRepository;

    public MaterialeController(MaterialeRepository materialeRepository) {
        this.materialeRepository = materialeRepository;
    }

    @GetMapping
    public List<Materiale> tutti() {
        return materialeRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SARTA', 'SUPER_ADMIN')")
    public Materiale crea(@RequestBody @Valid CreazioneMaterialeRequest request) {
        Materiale materiale = new Materiale();
        materiale.setNome(request.nome());
        materiale.setPrezzoAlMetro(request.prezzoAlMetro());
        materiale.setColori(
                request.colori().stream()
                        .map(c -> new Colore(c.nome(), c.hex()))
                        .collect(Collectors.toList())
        );
        return materialeRepository.save(materiale);
    }
}
