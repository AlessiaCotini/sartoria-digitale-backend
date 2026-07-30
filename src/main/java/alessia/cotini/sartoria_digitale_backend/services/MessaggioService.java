package alessia.cotini.sartoria_digitale_backend.services;

import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Messaggio;
import alessia.cotini.sartoria_digitale_backend.entities.Ordine;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.payloads.MessaggioResponse;
import alessia.cotini.sartoria_digitale_backend.repositories.MessaggioRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.OrdineRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessaggioService {

    private final MessaggioRepository messaggioRepository;
    private final OrdineRepository ordineRepository;
    private final UtenteRepository utenteRepository;

    public MessaggioService(MessaggioRepository messaggioRepository, OrdineRepository ordineRepository,
                            UtenteRepository utenteRepository) {
        this.messaggioRepository = messaggioRepository;
        this.ordineRepository = ordineRepository;
        this.utenteRepository = utenteRepository;
    }

    public Messaggio invia(UUID ordineId, UUID mittenteId, String testo) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato con id " + ordineId));
        Utente mittente = utenteRepository.findById(mittenteId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con id " + mittenteId));

        Messaggio messaggio = new Messaggio();
        messaggio.setOrdine(ordine);
        messaggio.setMittente(mittente);
        messaggio.setTesto(testo);
        messaggio.setLetto(false);

        return messaggioRepository.save(messaggio);
    }

    public List<Messaggio> storico(UUID ordineId) {
        return messaggioRepository.findByOrdineIdOrderByDataInvioAsc(ordineId);
    }

    public MessaggioResponse toResponse(Messaggio m) {
        return new MessaggioResponse(
                m.getId(),
                m.getOrdine().getId(),
                m.getMittente().getId(),
                m.getMittente().getNome() + " " + m.getMittente().getCognome(),
                m.getTesto(),
                m.getDataInvio()
        );
    }

    public long contaNonLetti(UUID utenteId) {
        return messaggioRepository.contaNonLettiPerUtente(utenteId);
    }

    public void segnaComeLetti(UUID ordineId, UUID utenteId) {
        List<Messaggio> daSegnare =
                messaggioRepository.findByOrdineIdAndMittenteIdNotAndLettoFalse(ordineId, utenteId);
        daSegnare.forEach(m -> m.setLetto(true));
        messaggioRepository.saveAll(daSegnare);
    }
}