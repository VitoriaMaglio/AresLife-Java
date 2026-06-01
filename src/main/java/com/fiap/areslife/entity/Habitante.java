package com.fiap.areslife.entity;

import com.fiap.areslife.enums.StatusHabitante;
import com.fiap.areslife.enums.TipoHabitante;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colonia_id", nullable = false)
    private Colonia colonia;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, insertable = false, updatable = false)
    private TipoHabitante tipo;

    @Column(nullable = false)
    private String nacionalidade;

    @Column(nullable = false)
    private LocalDate dataChegada;

    private LocalDate dataSaida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusHabitante status = StatusHabitante.ATIVO;

    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaudeHabitante> registrosSaude;

    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treinamento> treinamentos;

    @OneToMany(mappedBy = "habitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ViagemTuristica> viagens;
}
