package alessia.cotini.sartoria_digitale_backend.services;

import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import alessia.cotini.sartoria_digitale_backend.entities.Misure;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.enums.Ruolo;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistrazioneClienteRequest;
import alessia.cotini.sartoria_digitale_backend.repositories.MisureRepository;
import alessia.cotini.sartoria_digitale_backend.repositories.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final MisureRepository misureRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, MisureRepository misureRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.misureRepository = misureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utente findById(UUID id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nessun utente trovato con id " + id));
    }

    public Utente findByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Nessun utente trovato con email " + email));
    }

    public Utente registraCliente(RegistrazioneClienteRequest request) {
        if (utenteRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email già registrata: " + request.email());
        }

        Utente utente = new Utente();
        utente.setNome(request.nome());
        utente.setCognome(request.cognome());
        utente.setEmail(request.email());
        utente.setPassword(passwordEncoder.encode(request.password()));
        utente.setRuolo(Ruolo.CLIENTE);
        utente = utenteRepository.save(utente);

        Misure misure = new Misure();
        misure.setUtente(utente);
        misure.setAltezza(request.misure().altezza());
        misure.setPeso(request.misure().peso());
        misure.setTorace(request.misure().torace());
        misure.setVita(request.misure().vita());
        misure.setFianchi(request.misure().fianchi());
        misure.setSpalle(request.misure().spalle());
        misure.setManica(request.misure().manica());
        misure.setGamba(request.misure().gamba());
        misure.setCollo(request.misure().collo());
        misure.setBicipite(request.misure().bicipite());
        misure.setPolso(request.misure().polso());
        misure.setBusto(request.misure().busto());
        misure.setCoscia(request.misure().coscia());
        misure.setGinocchio(request.misure().ginocchio());
        misure.setCaviglia(request.misure().caviglia());
        misureRepository.save(misure);

        return utente;
    }
}
