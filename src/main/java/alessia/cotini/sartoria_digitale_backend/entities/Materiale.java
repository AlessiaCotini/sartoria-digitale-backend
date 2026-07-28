package alessia.cotini.sartoria_digitale_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "materiali")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Materiale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double prezzoAlMetro;

    @ElementCollection
    @CollectionTable(name = "materiale_colori", joinColumns = @JoinColumn(name = "materiale_id"))
    private List<Colore> colori = new ArrayList<>();
}
