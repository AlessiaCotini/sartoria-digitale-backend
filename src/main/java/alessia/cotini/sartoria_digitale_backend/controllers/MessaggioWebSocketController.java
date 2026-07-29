package alessia.cotini.sartoria_digitale_backend.controllers;

import alessia.cotini.sartoria_digitale_backend.entities.Messaggio;
import alessia.cotini.sartoria_digitale_backend.payloads.InvioMessaggioRequest;
import alessia.cotini.sartoria_digitale_backend.payloads.MessaggioResponse;
import alessia.cotini.sartoria_digitale_backend.services.MessaggioService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class MessaggioWebSocketController {

    private final MessaggioService messaggioService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessaggioWebSocketController(MessaggioService messaggioService,
                                        SimpMessagingTemplate messagingTemplate) {
        this.messaggioService = messaggioService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/ordini/{ordineId}/messaggi")
    public void invia(@DestinationVariable UUID ordineId, InvioMessaggioRequest request, Principal principal) {
        UUID mittenteId = UUID.fromString(principal.getName());
        Messaggio messaggio = messaggioService.invia(ordineId, mittenteId, request.testo());
        MessaggioResponse response = messaggioService.toResponse(messaggio);
        messagingTemplate.convertAndSend("/topic/ordini/" + ordineId, response);
    }
}
