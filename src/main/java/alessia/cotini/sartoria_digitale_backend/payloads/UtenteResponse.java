package alessia.cotini.sartoria_digitale_backend.payloads;

import alessia.cotini.sartoria_digitale_backend.enums.Ruolo;

import java.util.UUID;

public record UtenteResponse(
        UUID id,
        String nome,
        String cognome,
        String email,
        Ruolo ruolo
) {}
