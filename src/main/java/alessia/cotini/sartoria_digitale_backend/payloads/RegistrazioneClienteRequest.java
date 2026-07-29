package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrazioneClienteRequest( @NotBlank String nome,
                                           @NotBlank String cognome,
                                           @NotBlank @Email String email,
                                           @NotBlank String password,
                                           @Valid MisureRequest misure) {
}
