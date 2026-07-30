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
    private final UtenteRepository utenteRepository;
    private final OpzioneCapoRepository opzioneCapoRepository;

    public OrdineService(OrdineRepository ordineRepository, CapoRepository capoRepository,
                         MaterialeRepository materialeRepository, MisureRepository misureRepository, ClienteNegozioRepository clienteNegozioRepository, PagamentoService pagamentoService, UtenteRepository utenteRepository, OpzioneCapoRepository opzioneCapoRepository) {
        this.ordineRepository = ordineRepository;
        this.capoRepository = capoRepository;
        this.materialeRepository = materialeRepository;
        this.misureRepository = misureRepository;
        this.clienteNegozioRepository = clienteNegozioRepository;
        this.pagamentoService = pagamentoService;
        this.utenteRepository = utenteRepository;
        this.opzioneCapoRepository = opzioneCapoRepository;
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

        List<OpzioneCapo> opzioni = trovaOpzioni(request.opzioniIds());
        ordine.setOpzioniScelte(opzioni);
        ordine.setStato(StatoOrdine.PREVENTIVO_RICHIESTO);
        ordine.setPrezzoTotale(
                capo.getPrezzoDa() + materiale.getPrezzoAlMetro() * 3 + sommaSovrapprezzi(opzioni));

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
        if (nuovoStato == StatoOrdine.IN_LAVORAZIONE) {
            Pagamento pagamento = pagamentoService.trovaPerOrdine(ordineId);
            if (pagamento.getAccontoImporto() == null) {
                throw new BadRequestException("Non puoi avviare la lavorazione senza aver registrato l'acconto");
            }
        }
        ordine.setStato(nuovoStato);
        return ordineRepository.save(ordine);
    }

    public Ordine creaOrdineNegozio(CreazioneOrdineNegozioRequest request, Utente sarta) {
        Capo capo = capoRepository.findById(request.capoId())
                .orElseThrow(() -> new NotFoundException("Capo non trovato con id " + request.capoId()));
        Materiale materiale = materialeRepository.findById(request.materialeId())
                .orElseThrow(() -> new NotFoundException("Materiale non trovato con id " + request.materialeId()));

        Ordine ordine = new Ordine();

        if (request.clienteId() != null) {
            Utente cliente = utenteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new NotFoundException("Cliente non trovato con id " + request.clienteId()));
            Misure misure = misureRepository.findByUtenteId(cliente.getId())
                    .orElseThrow(() -> new BadRequestException("Nessuna misura trovata per questo cliente"));
            ordine.setCliente(cliente);
            ordine.setMisure(misure);
        } else if (request.clienteNegozioId() != null) {
            ClienteNegozio clienteNegozio = clienteNegozioRepository.findById(request.clienteNegozioId())
                    .orElseThrow(() -> new NotFoundException(
                            "Cliente di negozio non trovato con id " + request.clienteNegozioId()));
            ordine.setClienteNegozio(clienteNegozio);
        } else if (request.nomeClienteNuovo() != null && request.cognomeClienteNuovo() != null
                && request.telefonoClienteNuovo() != null && request.misureClienteNuovo() != null) {
            DatiMisure misure = new DatiMisure(
                    request.misureClienteNuovo().altezza(), request.misureClienteNuovo().peso(),
                    request.misureClienteNuovo().torace(), request.misureClienteNuovo().vita(),
                    request.misureClienteNuovo().fianchi(), request.misureClienteNuovo().spalle(),
                    request.misureClienteNuovo().manica(), request.misureClienteNuovo().gamba(),
                    request.misureClienteNuovo().collo(), request.misureClienteNuovo().bicipite(),
                    request.misureClienteNuovo().polso(), request.misureClienteNuovo().busto(),
                    request.misureClienteNuovo().coscia(), request.misureClienteNuovo().ginocchio(),
                    request.misureClienteNuovo().caviglia()
            );

            ClienteNegozio nuovo = new ClienteNegozio();
            nuovo.setNome(request.nomeClienteNuovo());
            nuovo.setCognome(request.cognomeClienteNuovo());
            nuovo.setTelefono(request.telefonoClienteNuovo());
            nuovo.setMisure(misure);
            nuovo.setRegistratoDa(sarta);
            nuovo = clienteNegozioRepository.save(nuovo);
            ordine.setClienteNegozio(nuovo);
        } else {
            throw new BadRequestException(
                    "Serve un cliente registrato, di negozio esistente, o i dati per crearne uno nuovo");
        }

        List<OpzioneCapo> opzioni = trovaOpzioni(request.opzioniIds());

        ordine.setCapo(capo);
        ordine.setMateriale(materiale);
        ordine.setColore(request.colore());
        ordine.setOpzioniScelte(opzioni);
        ordine.setStato(StatoOrdine.ACCETTATO);
        ordine.setAssegnatoA(sarta);
        ordine.setPrezzoTotale(
                capo.getPrezzoDa() + materiale.getPrezzoAlMetro() * 3 + sommaSovrapprezzi(opzioni));

        Ordine salvato = ordineRepository.save(ordine);
        pagamentoService.creaVuoto(salvato);
        return salvato;
    }

    private List<OpzioneCapo> trovaOpzioni(List<UUID> opzioniIds) {
        if (opzioniIds == null || opzioniIds.isEmpty()) {
            return List.of();
        }
        return opzioneCapoRepository.findAllById(opzioniIds);
    }

    private double sommaSovrapprezzi(List<OpzioneCapo> opzioni) {
        return opzioni.stream().mapToDouble(OpzioneCapo::getSovrapprezzo).sum();
    }

    public Ordine modificaPrezzo(UUID ordineId, Double nuovoPrezzo) {
        Ordine ordine = trovaPerId(ordineId);
        ordine.setPrezzoTotale(nuovoPrezzo);
        return ordineRepository.save(ordine);
    }

    public List<Ordine> trovaCodaMagazzino() {
        return ordineRepository.findByStatoIn(List.of(StatoOrdine.ACCETTATO, StatoOrdine.MATERIALI_ORDINATI));
    }

    public Ordine modificaFornitore(UUID ordineId, String fornitore) {
        Ordine ordine = trovaPerId(ordineId);
        ordine.setFornitore(fornitore);
        return ordineRepository.save(ordine);
    }
}
