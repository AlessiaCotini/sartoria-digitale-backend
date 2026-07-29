package alessia.cotini.sartoria_digitale_backend.entities;

import alessia.cotini.sartoria_digitale_backend.enums.StatoAppuntamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appuntamenti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appuntamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Utente cliente;

    //solo se il cliente è di negozio
    @ManyToOne
    @JoinColumn(name = "cliente_negozio_id")
    private ClienteNegozio clienteNegozio;

    // vuoto finché nessuna sarta lo conferma/prende in carico
    @ManyToOne
    @JoinColumn(name = "sarta_id")
    private Utente sarta;


    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoAppuntamento stato;

    private String note;
}
