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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;

    public AuthController(UtenteService utenteService, PasswordEncoder passwordEncoder, JWTTools jwtTools) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponse register(@RequestBody @Valid RegistrazioneClienteRequest request) {
        Utente utente = utenteService.registraCliente(request);
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