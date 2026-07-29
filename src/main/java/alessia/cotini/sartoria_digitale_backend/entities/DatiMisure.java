package alessia.cotini.sartoria_digitale_backend.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatiMisure {
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
}
