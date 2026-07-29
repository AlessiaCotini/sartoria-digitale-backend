package alessia.cotini.sartoria_digitale_backend.eccezioni;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
