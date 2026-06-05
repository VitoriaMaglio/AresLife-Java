package com.fiap.areslife.entity;

import com.fiap.areslife.enums.StatusHabitante;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "habitantes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habitante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_habitante")
    @SequenceGenerator(
            name = "seq_habitante",
            sequenceName = "SEQ_HABITANTE",
            allocationSize = 1
    )
    @Column(name = "ID_HABITANTE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLONIA", nullable = true)
    private Colonia colonia;

    @Column(nullable = false)
    private String nome;


    @Column(nullable = false)
    private String nacionalidade;

    @Column(nullable = false,name = "DATA_CHEGADA")
    private LocalDate dataChegada;
    @Column(name = "DATA_SAIDA")
    private LocalDate dataSaida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusHabitante status = StatusHabitante.ATIVO;

    @Column(name = "especialidade", length = 100)
    private String especialidade;


    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaudeHabitante> registrosSaude;

    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treinamento> treinamentos;

    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ViagemTuristica> viagens;
}
