package alessia.cotini.sartoria_digitale_backend.eccezioni;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
