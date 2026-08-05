package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.OpzioneAccessorio;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import alessia.cotini.sartoria_digitale_backend.repositories.OpzioneAccessorioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opzioni-accessori")
public class OpzioneAccessorioController {

    private final OpzioneAccessorioRepository opzioneAccessorioRepository;

    public OpzioneAccessorioController(OpzioneAccessorioRepository opzioneAccessorioRepository) {
        this.opzioneAccessorioRepository = opzioneAccessorioRepository;
    }

    @GetMapping
    public List<OpzioneAccessorio> perTipo(@RequestParam TipoAccessorio tipo) {
        return opzioneAccessorioRepository.findByTipiApplicabiliContaining(tipo);
    }
}
