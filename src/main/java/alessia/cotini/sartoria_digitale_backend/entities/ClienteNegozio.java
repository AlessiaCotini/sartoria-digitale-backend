package alessia.cotini.sartoria_digitale_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clienti_negozio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteNegozio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private String telefono;

    @Embedded
    private DatiMisure misure;

    // la sarta che l'ha inserito
    @ManyToOne(optional = false)
    @JoinColumn(name = "registrato_da_id")
    private Utente registratoDa;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCreazione;
}
