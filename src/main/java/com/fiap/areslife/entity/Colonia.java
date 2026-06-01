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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Localizacao localizacao;

    @Column(nullable = false)
    private Integer capacidadeMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusColonia status = StatusColonia.ATIVA;

    @Column(nullable = false)
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
