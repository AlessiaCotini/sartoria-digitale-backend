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

    // nome del colore scelto tra quelli del materiale (es. "Bordeaux")
    private String colore;

    // le misure del cliente al momento dell'ordine — riferimento diretto per ora,
    // valutiamo più avanti se serve "fotografarle" per non farle cambiare
    // retroattivamente se il cliente aggiorna il profilo dopo
    @ManyToOne(optional = false)
    @JoinColumn(name = "misure_id")
    private Misure misure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato;

    // sarta o sottoposto a cui è assegnato l'ordine (può essere nullo finché non assegnato)
    @ManyToOne
    @JoinColumn(name = "assegnato_a_id")
    private Utente assegnatoA;

    @Column(nullable = false)
    private Double prezzoTotale;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCreazione;
}
