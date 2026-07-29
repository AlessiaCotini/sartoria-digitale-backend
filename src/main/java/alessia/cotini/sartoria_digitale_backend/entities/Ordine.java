package alessia.cotini.sartoria_digitale_backend.entities;

import alessia.cotini.sartoria_digitale_backend.enums.StatoOrdine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Utente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "capo_id")
    private Capo capo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    private String colore;

    @ManyToOne(optional = false)
    @JoinColumn(name = "misure_id")
    private Misure misure;

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
