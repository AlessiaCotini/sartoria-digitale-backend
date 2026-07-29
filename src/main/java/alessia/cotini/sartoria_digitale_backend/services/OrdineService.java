package alessia.cotini.sartoria_digitale_backend.services;
import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.*;
import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneOrdineNegozioRequest;
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
    private final ClienteNegozioRepository clienteNegozioRepository;
    private final PagamentoService pagamentoService;

    public OrdineService(OrdineRepository ordineRepository, CapoRepository capoRepository,
                         MaterialeRepository materialeRepository, MisureRepository misureRepository, ClienteNegozioRepository clienteNegozioRepository, PagamentoService pagamentoService) {
        this.ordineRepository = ordineRepository;
        this.capoRepository = capoRepository;
        this.materialeRepository = materialeRepository;
        this.misureRepository = misureRepository;
        this.clienteNegozioRepository = clienteNegozioRepository;
        this.pagamentoService = pagamentoService;
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

        Ordine salvato = ordineRepository.save(ordine);
        pagamentoService.creaVuoto(salvato);
        return salvato;
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

    public Ordine creaOrdineNegozio(CreazioneOrdineNegozioRequest request, Utente sarta) {
        Capo capo = capoRepository.findById(request.capoId())
                .orElseThrow(() -> new NotFoundException("Capo non trovato con id " + request.capoId()));
        Materiale materiale = materialeRepository.findById(request.materialeId())
                .orElseThrow(() -> new NotFoundException("Materiale non trovato con id " + request.materialeId()));

        DatiMisure misure = new DatiMisure(
                request.misure().altezza(), request.misure().peso(), request.misure().torace(),
                request.misure().vita(), request.misure().fianchi(), request.misure().spalle(),
                request.misure().manica(), request.misure().gamba(), request.misure().collo(),
                request.misure().bicipite(), request.misure().polso(), request.misure().busto(),
                request.misure().coscia(), request.misure().ginocchio(), request.misure().caviglia()
        );

        ClienteNegozio clienteNegozio = new ClienteNegozio();
        clienteNegozio.setNome(request.nomeCliente());
        clienteNegozio.setCognome(request.cognomeCliente());
        clienteNegozio.setTelefono(request.telefonoCliente());
        clienteNegozio.setMisure(misure);
        clienteNegozio.setRegistratoDa(sarta);
        clienteNegozio = clienteNegozioRepository.save(clienteNegozio);

        Ordine ordine = new Ordine();
        ordine.setClienteNegozio(clienteNegozio);
        ordine.setCapo(capo);
        ordine.setMateriale(materiale);
        ordine.setColore(request.colore());
        ordine.setStato(StatoOrdine.PREVENTIVO_RICHIESTO);
        ordine.setAssegnatoA(sarta);
        ordine.setPrezzoTotale(capo.getPrezzoDa() + materiale.getPrezzoAlMetro() * 3);

        Ordine salvato = ordineRepository.save(ordine);
        pagamentoService.creaVuoto(salvato);
        return salvato;
    }
}
