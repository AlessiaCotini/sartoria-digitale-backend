package alessia.cotini.sartoria_digitale_backend.repositories;

import java.util.UUID;

public interface ConteggioPerOrdine {
    UUID getOrdineId();
    Long getConteggio();
}
