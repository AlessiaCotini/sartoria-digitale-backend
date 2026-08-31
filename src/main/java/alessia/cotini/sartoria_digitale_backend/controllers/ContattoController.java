package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.payloads.RichiestaContattoRequest;
import alessia.cotini.sartoria_digitale_backend.services.MailgunService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contatti")
public class ContattoController {

    private final MailgunService mailgunService;
    private final String emailContatto;

    public ContattoController(MailgunService mailgunService,
                              @Value("${contatto.email}") String emailContatto) {
        this.mailgunService = mailgunService;
        this.emailContatto = emailContatto;
    }

    @PostMapping
    public void invia(@RequestBody @Valid RichiestaContattoRequest request) {
        mailgunService.invia(
                emailContatto,
                "Nuova richiesta dal sito — " + request.tipoServizio(),
                "Nome: " + request.nome() + "\n" +
                        "Email: " + request.email() + "\n" +
                        "Servizio richiesto: " + request.tipoServizio() + "\n\n" +
                        "Messaggio:\n" + request.messaggio()
        );
    }
}
