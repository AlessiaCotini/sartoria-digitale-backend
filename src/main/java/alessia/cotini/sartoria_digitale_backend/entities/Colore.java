package alessia.cotini.sartoria_digitale_backend.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Colore {
    private String nome;
    private String hex;
}
