package alessia.cotini.sartoria_digitale_backend.entities;

import alessia.cotini.sartoria_digitale_backend.enums.MetodoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pagamenti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "ordine_id", nullable = false, unique = true)
    private Ordine ordine;

    private Double accontoImporto;
    private LocalDate accontoData;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento accontoMetodo;

    private Double saldoImporto;
    private LocalDate saldoData;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento saldoMetodo;
}
