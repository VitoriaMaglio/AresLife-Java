package com.fiap.areslife.entity;

import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusColonia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "colonias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_colonia")
    @SequenceGenerator(
            name = "seq_colonia",
            sequenceName = "SEQ_COLONIA",
            allocationSize = 1
    )
    @Column(name = "ID_COLONIA")
    private Long id;

    @Column(name = "NOME",nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOCALIZACAO",nullable = false)
    private Localizacao localizacao;

    @Column(name = "CAPACIDADE_MAX",nullable = false)
    private Integer capacidadeMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusColonia status = StatusColonia.ATIVA;

    @Column(name = "DATA_FUNDACAO",nullable = false)
    private LocalDate dataFundacao;

    @Column(length = 500)
    private String descricao;

    @OneToMany(mappedBy = "colonia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recurso> recursos;

    @OneToMany(mappedBy = "colonia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Habitante> habitantes;

    @OneToMany(mappedBy = "colonia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alerta> alertas;
}
