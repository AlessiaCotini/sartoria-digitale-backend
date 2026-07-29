package alessia.cotini.sartoria_digitale_backend.payloads;

public record MisureRequest(
        Double altezza,
        Double peso,
        Double torace,
        Double vita,
        Double fianchi,
        Double spalle,
        Double manica,
        Double gamba,
        Double collo,
        Double bicipite,
        Double polso,
        Double busto,
        Double coscia,
        Double ginocchio,
        Double caviglia
) {}
