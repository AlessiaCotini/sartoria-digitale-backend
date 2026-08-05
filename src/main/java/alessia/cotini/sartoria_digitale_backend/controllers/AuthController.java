package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.payloads.LoginRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.LoginResponse;
import alessia.cotini.sartoria_digitale_backend.payloads.RegistrazioneClienteRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.UtenteResponse;
import alessia.cotini.sartoria_digitale_backend.security.JWTTools;
import alessia.cotini.sartoria_digitale_backend.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import alessia.cotini.sartoria_digitale_backend.services.MailgunService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;
    private final MailgunService mailgunService;

    public AuthController(UtenteService utenteService, PasswordEncoder passwordEncoder, JWTTools jwtTools, MailgunService mailgunService) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
        this.mailgunService = mailgunService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponse register(@RequestBody @Valid RegistrazioneClienteRequest request) {
        Utente utente = utenteService.registraCliente(request);
        mailgunService.invia(
                utente.getEmail(),
                "Benvenuta in Bellariva",
                "Ciao " + utente.getNome() + ",\n\nGrazie per esserti registrata su Bellariva. Da ora puoi sfogliare la collezione, configurare i tuoi capi su misura e gestire i tuoi ordini dal tuo profilo.\n\nA presto,\nBellariva"
        );
        return new UtenteResponse(utente.getId(), utente.getNome(), utente.getCognome(), utente.getEmail(), utente.getRuolo());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        Utente utente = utenteService.findByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), utente.getPassword())) {
            throw new BadRequestException("Email o password errati");
        }

        String token = jwtTools.generoToken(utente);
        return new LoginResponse(token);
    }
}