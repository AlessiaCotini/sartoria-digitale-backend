package alessia.cotini.sartoria_digitale_backend.payloads;

import jakarta.validation.constraints.NotBlank;

public record ColoreRequest(@NotBlank String nome, @NotBlank String hex) {}
