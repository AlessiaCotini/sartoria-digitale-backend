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
@Table(name = "messaggi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Messaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordine_id")
    private Ordine ordine;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mittente_id")
    private Utente mittente;

    @Column(nullable = false, length = 2000)
    private String testo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataInvio;

    private boolean letto;
}
