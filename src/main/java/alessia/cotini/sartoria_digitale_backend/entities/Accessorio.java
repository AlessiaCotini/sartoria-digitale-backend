package alessia.cotini.sartoria_digitale_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import alessia.cotini.sartoria_digitale_backend.enums.Genere;
import alessia.cotini.sartoria_digitale_backend.enums.TipoAccessorio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

    @Entity
    @Table(name = "accessori")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Accessorio {

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
        private TipoAccessorio tipo;

        private String modello;

        private String tessuto;

        @Column(nullable = false)
        private Double prezzoDa;

        private boolean inEvidenza;
    }

