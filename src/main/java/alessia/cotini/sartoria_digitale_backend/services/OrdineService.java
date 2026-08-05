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
    private final AccessorioRepository accessorioRepository;
    private final MaterialeRepository materialeRepository;
    private final MisureRepository misureRepository;
    private final ClienteNegozioRepository clienteNegozioRepository;
    private final PagamentoService pagamentoService;
    private final UtenteRepository utenteRepository;
    private final OpzioneCapoRepository opzioneCapoRepository;
    private final OpzioneAccessorioRepository opzioneAccessorioRepository;
    private final MailgunService mailgunService;

    public OrdineService(OrdineRepository ordineRepository, CapoRepository capoRepository,
                         AccessorioRepository accessorioRepository,
                         MaterialeRepository materialeRepository, MisureRepository misureRepository,
                         ClienteNegozioRepository clienteNegozioRepository, PagamentoService pagamentoService,
                         UtenteRepository utenteRepository, OpzioneCapoRepository opzioneCapoRepository,
                         OpzioneAccessorioRepository opzioneAccessorioRepository, MailgunService mailgunService) {
        this.ordineRepository = ordineRepository;
        this.capoRepository = capoRepository;
        this.accessorioRepository = accessorioRepository;
        this.materialeRepository = materialeRepository;
        this.misureRepository = misureRepository;
        this.clienteNegozioRepository = clienteNegozioRepository;
        this.pagamentoService = pagamentoService;
        this.utenteRepository = utenteRepository;
        this.opzioneCapoRepository = opzioneCapoRepository;
        this.opzioneAccessorioRepository = opzioneAccessorioRepository;
        this.mailgunService = mailgunService;
    }

    private record OggettoOrdinato(String nome, double prezzoBase) {}

    private OggettoOrdinato applicaCapoOAccessorio(Ordine ordine, UUID capoId, UUID accessorioId,
                                                   List<OpzioneCapo> opzioniCapo, List<OpzioneAccessorio> opzioniAccessorio) {
        if (capoId != null && accessorioId != null) {
            throw new BadRequestException("Un ordine può riferirsi a un capo oppure a un accessorio, non entrambi");
        }
        if (capoId != null) {
            Capo capo = capoRepository.findById(capoId)
                    .orElseThrow(() -> new NotFoundException("Capo non trovato con id " + capoId));
            ordine.setCapo(capo);
            ordine.setOpzioniScelte(opzioniCapo);
            return new OggettoOrdinato(capo.getNome(), capo.getPrezzoDa());
        }
        if (accessorioId != null) {
            Accessorio accessorio = accessorioRepository.findById(accessorioId)
                    .orElseThrow(() -> new NotFoundException("Accessorio non trovato con id " + accessorioId));
            ordine.setAccessorio(accessorio);
            ordine.setOpzioniAccessorioScelte(opzioniAccessorio);
            return new OggettoOrdinato(accessorio.getNome(), accessorio.getPrezzoDa());
        }
        throw new BadRequestException("Serve un capo o un accessorio da ordinare");
    }

    public Ordine creaOrdine(CreazioneOrdineRequest request, Utente cliente) {
        Materiale materiale = materialeRepository.findById(request.materialeId())
                .orElseThrow(() -> new NotFoundException("Materiale non trovato con id " + request.materialeId()));

        Ordine ordine = new Ordine();
        ordine.setCliente(cliente);
        ordine.setMateriale(materiale);
        ordine.setColore(request.colore());

        if (request.capoId() != null) {
            Misure misure = misureRepository.findByUtenteId(cliente.getId())
                    .orElseThrow(() -> new BadRequestException("Nessuna misura trovata per questo cliente"));
            ordine.setMisure(misure);
        }

        List<OpzioneCapo> opzioniCapo = trovaOpzioni(request.opzioniIds());
        List<OpzioneAccessorio> opzioniAccessorio = trovaOpzioniAccessorio(request.opzioniAccessorioIds());
        OggettoOrdinato oggetto = applicaCapoOAccessorio(ordine, request.capoId(), request.accessorioId(), opzioniCapo, opzioniAccessorio);

        ordine.setStato(StatoOrdine.PREVENTIVO_RICHIESTO);
        ordine.setPrezzoTotale(
                oggetto.prezzoBase() + materiale.getPrezzoAlMetro() * (request.capoId() != null ? 3 : 1)
                        + sommaSovrapprezzi(opzioniCapo) + sommaSovrapprezziAccessorio(opzioniAccessorio));

        Ordine salvato = ordineRepository.save(ordine);
        pagamentoService.creaVuoto(salvato);
        mailgunService.invia(
                cliente.getEmail(),
                "Richiesta preventivo ricevuta",
                "Ciao " + cliente.getNome() + ",\n\nAbbiamo ricevuto la tua richiesta di preventivo per " + oggetto.nome() + ". La sarta la esaminerà e ti risponderà a breve in chat dal tuo profilo.\n\nBellariva"
        );
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
        Ordine salvato = ordineRepository.save(ordine);
        if (ordine.getCliente() != null) {
            String nomeOggetto = ordine.getCapo() != null ? ordine.getCapo().getNome() : ordine.getAccessorio().getNome();
            mailgunService.invia(
                    ordine.getCliente().getEmail(),
                    "Aggiornamento sul tuo ordine",
                    "Ciao " + ordine.getCliente().getNome() + ",\n\nLo stato del tuo ordine per " + nomeOggetto + " è cambiato in: " + nuovoStato + ".\n\nBellariva"
            );
        }
        return salvato;
    }

    public Ordine creaOrdineNegozio(CreazioneOrdineNegozioRequest request, Utente sarta) {
        Materiale materiale = materialeRepository.findById(request.materialeId())
                .orElseThrow(() -> new NotFoundException("Materiale non trovato con id " + request.materialeId()));

        Ordine ordine = new Ordine();

        if (request.clienteId() != null) {
            Utente cliente = utenteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new NotFoundException("Cliente non trovato con id " + request.clienteId()));
            ordine.setCliente(cliente);
            if (request.capoId() != null) {
                Misure misure = misureRepository.findByUtenteId(cliente.getId())
                        .orElseThrow(() -> new BadRequestException("Nessuna misura trovata per questo cliente"));
                ordine.setMisure(misure);
            }
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

        List<OpzioneCapo> opzioniCapo = trovaOpzioni(request.opzioniIds());
        List<OpzioneAccessorio> opzioniAccessorio = trovaOpzioniAccessorio(request.opzioniAccessorioIds());
        OggettoOrdinato oggetto = applicaCapoOAccessorio(ordine, request.capoId(), request.accessorioId(), opzioniCapo, opzioniAccessorio);

        ordine.setMateriale(materiale);
        ordine.setColore(request.colore());
        ordine.setStato(StatoOrdine.ACCETTATO);
        ordine.setAssegnatoA(sarta);
        ordine.setPrezzoTotale(
                oggetto.prezzoBase() + materiale.getPrezzoAlMetro() * (request.capoId() != null ? 3 : 1)
                        + sommaSovrapprezzi(opzioniCapo) + sommaSovrapprezziAccessorio(opzioniAccessorio));

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

    private List<OpzioneAccessorio> trovaOpzioniAccessorio(List<UUID> opzioniIds) {
        if (opzioniIds == null || opzioniIds.isEmpty()) {
            return List.of();
        }
        return opzioneAccessorioRepository.findAllById(opzioniIds);
    }

    private double sommaSovrapprezzi(List<OpzioneCapo> opzioni) {
        return opzioni.stream().mapToDouble(OpzioneCapo::getSovrapprezzo).sum();
    }

    private double sommaSovrapprezziAccessorio(List<OpzioneAccessorio> opzioni) {
        return opzioni.stream().mapToDouble(OpzioneAccessorio::getSovrapprezzo).sum();
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