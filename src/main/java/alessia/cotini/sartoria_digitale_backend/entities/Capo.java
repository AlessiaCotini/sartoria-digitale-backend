package alessia.cotini.sartoria_digitale_backend.entities;

import alessia.cotini.sartoria_digitale_backend.enums.CategoriaCapo;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "capi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Capo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genere genere;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaCapo categoria;

    private String modello;

    private String tessuto;

    @Column(nullable = false)
    private Double prezzoDa;

    private boolean inEvidenza;

    private String immagine;
}
