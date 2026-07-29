package alessia.cotini.sartoria_digitale_backend.runners;

import alessia.cotini.sartoria_digitale_backend.entities.Capo;
import alessia.cotini.sartoria_digitale_backend.entities.Colore;
import alessia.cotini.sartoria_digitale_backend.entities.Materiale;
import alessia.cotini.sartoria_digitale_backend.repositories.CapoRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.MaterialeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CatalogoSeeder implements CommandLineRunner {

    private final CapoRepository capoRepository;
    private final MaterialeRepository materialeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CatalogoSeeder(CapoRepository capoRepository, MaterialeRepository materialeRepository) {
        this.capoRepository = capoRepository;
        this.materialeRepository = materialeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (capoRepository.count() == 0) {
            try (InputStream input = new ClassPathResource("data/capi.json").getInputStream()) {
                CapoSeed[] seeds = objectMapper.readValue(input, CapoSeed[].class);
                var capi = Arrays.stream(seeds).map(s -> {
                    Capo capo = new Capo();
                    capo.setNome(s.nome());
                    capo.setGenere(s.genere());
                    capo.setCategoria(s.categoria());
                    capo.setModello(s.modello());
                    capo.setTessuto(s.tessuto());
                    capo.setPrezzoDa(s.prezzoDa());
                    capo.setInEvidenza(s.inEvidenza());
                    capo.setImmagine(s.immagine());
                    return capo;
                }).collect(Collectors.toList());
                capoRepository.saveAll(capi);
                System.out.println("Catalogo capi popolato: " + capi.size() + " voci");
            }
        }

        if (materialeRepository.count() == 0) {
            try (InputStream input = new ClassPathResource("data/materiali.json").getInputStream()) {
                MaterialeSeed[] seeds = objectMapper.readValue(input, MaterialeSeed[].class);
                var materiali = Arrays.stream(seeds).map(s -> {
                    Materiale materiale = new Materiale();
                    materiale.setNome(s.nome());
                    materiale.setPrezzoAlMetro(s.prezzoAlMetro());
                    materiale.setColori(
                            s.colori().stream().map(c -> new Colore(c.nome(), c.hex())).collect(Collectors.toList())
                    );
                    return materiale;
                }).collect(Collectors.toList());
                materialeRepository.saveAll(materiali);
                System.out.println("Materiali popolati: " + materiali.size() + " voci");
            }
        }
    }
}
