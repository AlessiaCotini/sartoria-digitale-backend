package alessia.cotini.sartoria_digitale_backend.entities;

import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
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
@Table(name = "opzioni_capo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpzioneCapo {

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

    @ElementCollection(targetClass = CategoriaCapo.class)
    @CollectionTable(name = "opzione_categorie", joinColumns = @JoinColumn(name = "opzione_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private List<CategoriaCapo> categorieApplicabili = new ArrayList<>();
}
