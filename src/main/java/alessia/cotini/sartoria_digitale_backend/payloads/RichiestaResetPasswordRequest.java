package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RichiestaResetPasswordRequest(@NotBlank @Email String email) {}
