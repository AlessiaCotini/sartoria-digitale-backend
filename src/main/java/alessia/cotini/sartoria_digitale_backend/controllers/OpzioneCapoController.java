package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.OpzioneCapo;
import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.repositories.OpzioneCapoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opzioni")
public class OpzioneCapoController {

    private final OpzioneCapoRepository opzioneCapoRepository;

    public OpzioneCapoController(OpzioneCapoRepository opzioneCapoRepository) {
        this.opzioneCapoRepository = opzioneCapoRepository;
    }

    @GetMapping
    public List<OpzioneCapo> perCategoria(@RequestParam CategoriaCapo categoria) {
        return opzioneCapoRepository.findByCategorieApplicabiliContaining(categoria);
    }
}
