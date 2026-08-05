package alessia.cotini.sartoria_digitale_backend.services;

import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Appuntamento;
import alessia.cotini.sartoria_digitale_backend.entities.ClienteNegozio;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.enums.StatoAppuntamento;
import alessia.cotini.sartoria_digitale_backend.payloads.CreazioneAppuntamentoNegozioRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.ModificaAppuntamentoRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.RichiestaAppuntamentoRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.AppuntamentoRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.ClienteNegozioRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppuntamentoService {

    private final AppuntamentoRepository appuntamentoRepository;
    private final UtenteRepository utenteRepository;
    private final ClienteNegozioRepository clienteNegozioRepository;
    private final MailgunService mailgunService;

    public AppuntamentoService(AppuntamentoRepository appuntamentoRepository, UtenteRepository utenteRepository,
                               ClienteNegozioRepository clienteNegozioRepository, MailgunService mailgunService) {
        this.appuntamentoRepository = appuntamentoRepository;
        this.utenteRepository = utenteRepository;
        this.clienteNegozioRepository = clienteNegozioRepository;
        this.mailgunService = mailgunService;
    }

    public Appuntamento richiedi(RichiestaAppuntamentoRequest request, Utente cliente) {
        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setCliente(cliente);
        appuntamento.setDataOra(request.dataOra());
        appuntamento.setDataOraFine(request.dataOraFine());
        appuntamento.setNote(request.note());
        appuntamento.setStato(StatoAppuntamento.RICHIESTO);
        return appuntamentoRepository.save(appuntamento);
    }

    public Appuntamento creaPerSarta(CreazioneAppuntamentoNegozioRequest request, Utente sarta) {
        Appuntamento appuntamento = new Appuntamento();

        if (request.clienteId() != null) {
            Utente cliente = utenteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new NotFoundException("Cliente non trovato con id " + request.clienteId()));
            appuntamento.setCliente(cliente);
        } else if (request.clienteNegozioId() != null) {
            ClienteNegozio clienteNegozio = clienteNegozioRepository.findById(request.clienteNegozioId())
                    .orElseThrow(() -> new NotFoundException("Cliente di negozio non trovato con id " + request.clienteNegozioId()));
            appuntamento.setClienteNegozio(clienteNegozio);
        } else if (request.nomeClienteNuovo() != null && request.cognomeClienteNuovo() != null
                && request.telefonoClienteNuovo() != null) {
            ClienteNegozio nuovo = new ClienteNegozio();
            nuovo.setNome(request.nomeClienteNuovo());
            nuovo.setCognome(request.cognomeClienteNuovo());
            nuovo.setTelefono(request.telefonoClienteNuovo());
            nuovo.setRegistratoDa(sarta);
            nuovo = clienteNegozioRepository.save(nuovo);
            appuntamento.setClienteNegozio(nuovo);
        } else {
            throw new BadRequestException(
                    "Serve un cliente registrato, di negozio esistente, o i dati per crearne uno nuovo");
        }

        appuntamento.setSarta(sarta);
        appuntamento.setDataOra(request.dataOra());
        appuntamento.setDataOraFine(request.dataOraFine());
        appuntamento.setNote(request.note());
        appuntamento.setStato(StatoAppuntamento.CONFERMATO);
        Appuntamento salvato = appuntamentoRepository.save(appuntamento);
        if (salvato.getCliente() != null) {
            mailgunService.invia(
                    salvato.getCliente().getEmail(),
                    "Appuntamento confermato",
                    "Ciao " + salvato.getCliente().getNome() + ",\n\nIl tuo appuntamento in negozio è confermato per il " + salvato.getDataOra() + ".\n\nBellariva"
            );
        }
        return salvato;
    }

    public Appuntamento modifica(UUID id, ModificaAppuntamentoRequest request, Utente sarta) {
        Appuntamento appuntamento = trovaPerId(id);
        boolean diventaConfermato = request.stato() == StatoAppuntamento.CONFERMATO
                && appuntamento.getStato() != StatoAppuntamento.CONFERMATO;
        if (request.dataOra() != null) appuntamento.setDataOra(request.dataOra());
        if (request.dataOraFine() != null) appuntamento.setDataOraFine(request.dataOraFine());
        if (request.stato() != null) appuntamento.setStato(request.stato());
        if (request.note() != null) appuntamento.setNote(request.note());
        if (appuntamento.getSarta() == null) appuntamento.setSarta(sarta);
        Appuntamento salvato = appuntamentoRepository.save(appuntamento);
        if (diventaConfermato && salvato.getCliente() != null) {
            mailgunService.invia(
                    salvato.getCliente().getEmail(),
                    "Appuntamento confermato",
                    "Ciao " + salvato.getCliente().getNome() + ",\n\nIl tuo appuntamento è confermato per il " + salvato.getDataOra() + ".\n\nBellariva"
            );
        }
        return salvato;
    }

    public Appuntamento trovaPerId(UUID id) {
        return appuntamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appuntamento non trovato con id " + id));
    }

    public List<Appuntamento> trovaDelCliente(UUID clienteId) {
        return appuntamentoRepository.findByClienteId(clienteId);
    }

    public List<Appuntamento> trovaTutti() {
        return appuntamentoRepository.findAll();
    }
}
