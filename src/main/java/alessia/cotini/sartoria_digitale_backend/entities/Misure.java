package alessia.cotini.sartoria_digitale_backend.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "misure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Misure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double altezza;
    private Double peso;
    private Double torace;
    private Double vita;
    private Double fianchi;
    private Double spalle;
    private Double manica;
    private Double gamba;
    private Double collo;
    private Double bicipite;
    private Double polso;
    private Double busto;
    private Double coscia;
    private Double ginocchio;
    private Double caviglia;

    @OneToOne
    @JoinColumn(name = "utente_id", nullable = false, unique = true)
    private Utente utente;
}