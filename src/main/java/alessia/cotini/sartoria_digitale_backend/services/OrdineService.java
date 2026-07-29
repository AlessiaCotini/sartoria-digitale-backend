package alessia.cotini.sartoria_digitale_backend.services;
import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.*;
import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneOrdineRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final CapoRepository capoRepository;
    private final MaterialeRepository materialeRepository;
    private final MisureRepository misureRepository;

    public OrdineService(OrdineRepository ordineRepository, CapoRepository capoRepository,
                         MaterialeRepository materialeRepository, MisureRepository misureRepository) {
        this.ordineRepository = ordineRepository;
        this.capoRepository = capoRepository;
        this.materialeRepository = materialeRepository;
        this.misureRepository = misureRepository;
    }

    public Ordine creaOrdine(CreazioneOrdineRequest request, Utente cliente) {
        Capo capo = capoRepository.findById(request.capoId())
                .orElseThrow(() -> new NotFoundException("Capo non trovato con id " + request.capoId()));
        Materiale materiale = materialeRepository.findById(request.materialeId())
                .orElseThrow(() -> new NotFoundException("Materiale non trovato con id " + request.materialeId()));
        Misure misure = misureRepository.findByUtenteId(cliente.getId())
                .orElseThrow(() -> new BadRequestException("Nessuna misura trovata per questo cliente"));

        Ordine ordine = new Ordine();
        ordine.setCliente(cliente);
        ordine.setCapo(capo);
        ordine.setMateriale(materiale);
        ordine.setColore(request.colore());
        ordine.setMisure(misure);
        ordine.setStato(StatoOrdine.PREVENTIVO_RICHIESTO);
        ordine.setPrezzoTotale(capo.getPrezzoDa() + materiale.getPrezzoAlMetro() * 3);

        return ordineRepository.save(ordine);
    }

    public List<Ordine> trovaDelCliente(UUID clienteId) {
        return ordineRepository.findByClienteId(clienteId);
    }

    public List<Ordine> trovaAssegnati(UUID utenteId) {
        return ordineRepository.findByAssegnatoAId(utenteId);
    }

    public List<Ordine> trovaTutti() {
        return ordineRepository.findAll();
    }

    public Ordine trovaPerId(UUID id) {
        return ordineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato con id " + id));
    }

    public Ordine assegna(UUID ordineId, Utente assegnatoA) {
        Ordine ordine = trovaPerId(ordineId);
        ordine.setAssegnatoA(assegnatoA);
        return ordineRepository.save(ordine);
    }

    public Ordine cambiaStato(UUID ordineId, StatoOrdine nuovoStato) {
        Ordine ordine = trovaPerId(ordineId);
        ordine.setStato(nuovoStato);
        return ordineRepository.save(ordine);
    }
}
