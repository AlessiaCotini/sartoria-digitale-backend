package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreazioneUtenteRequest(
        @NotBlank String nome,
        @NotBlank String cognome,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
