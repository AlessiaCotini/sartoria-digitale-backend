package alessia.cotini.sartoria_digitale_backend.gestioneerrori;

import alessia.cotini.sartoria_digitale_backend.eccezioni.BadRequestException;
import alessia.cotini.sartoria_digitale_backend.eccezioni.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GestoreEccezioni {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> gestisciNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpoErrore(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> gestisciBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErrore(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> gestisciAccessoNegato(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(corpoErrore("Non hai i permessi per eseguire questa operazione."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> gestisciValidazione(MethodArgumentNotValidException e) {
        String messaggio = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Dati non validi");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErrore(messaggio));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> gestisciGenerica(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpoErrore(e.getMessage()));
    }

    private Map<String, Object> corpoErrore(String messaggio) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "errore", messaggio
        );
    }
}