package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RichiestaContattoRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String tipoServizio,
        @NotBlank String messaggio
) {}
