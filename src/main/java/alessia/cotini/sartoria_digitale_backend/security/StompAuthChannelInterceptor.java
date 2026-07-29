package alessia.cotini.sartoria_digitale_backend.security;

import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.services.UtenteService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    public StompAuthChannelInterceptor(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");
                jwtTools.verificoToken(token);
                UUID utenteId = jwtTools.checkIdDalToken(token);
                Utente utente = utenteService.findById(utenteId);
                accessor.setUser((Principal) () -> utente.getId().toString());
            }
        }

        return message;
    }
}