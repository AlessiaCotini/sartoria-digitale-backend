package alessia.cotini.sartoria_digitale_backend.entities;
import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordini")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Utente cliente;

    @ManyToOne
    @JoinColumn(name = "cliente_negozio_id")
    private ClienteNegozio clienteNegozio;

    @ManyToOne
    @JoinColumn(name = "capo_id")
    private Capo capo;

    @ManyToOne
    @JoinColumn(name = "accessorio_id")
    private Accessorio accessorio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    @ManyToOne
    @JoinColumn(name = "misure_id")
    private Misure misure;

    private String colore;

    private String fornitore;

    @ManyToMany
    @JoinTable(
            name = "ordine_opzioni",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "opzione_id")
    )
    private List<OpzioneCapo> opzioniScelte = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "ordine_opzioni_accessorio",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "opzione_id")
    )
    private List<OpzioneAccessorio> opzioniAccessorioScelte = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato;

    @ManyToOne
    @JoinColumn(name = "assegnato_a_id")
    private Utente assegnatoA;

    @Column(nullable = false)
    private Double prezzoTotale;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCreazione;
}