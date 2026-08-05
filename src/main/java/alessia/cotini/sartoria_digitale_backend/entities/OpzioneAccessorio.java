package alessia.cotini.sartoria_digitale_backend.entities;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import alessia.cotini.sartoria_digitale_backend.enums.TipoOpzione;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "opzioni_accessorio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpzioneAccessorio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOpzione tipo;

    @Column(nullable = false)
    private Double sovrapprezzo;

    @ElementCollection(targetClass = TipoAccessorio.class)
    @CollectionTable(name = "opzione_accessorio_tipi", joinColumns = @JoinColumn(name = "opzione_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accessorio")
    private List<TipoAccessorio> tipiApplicabili = new ArrayList<>();
}
