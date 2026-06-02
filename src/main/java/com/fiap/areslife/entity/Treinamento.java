package com.fiap.areslife.entity;

import com.fiap.areslife.enums.StatusTreinamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "treinamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treinamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_treinamento")
    @SequenceGenerator(
            name = "seq_treinamento",
            sequenceName = "SEQ_TREINAMENTO",
            allocationSize = 1
    )
    @Column(name = "ID_TREINAMENTO")

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HABITANTE", nullable = false)
    private Habitante habitante;

    @Column(name = "TIPO_TREINAMENTO",nullable = false)
    private String tipoTreinamento;

    @Column(name = "CARGA_HORARIA",nullable = false)
    private Integer cargaHoraria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTreinamento status;
    @Column(name = "DATA_INICIO")
    private LocalDate dataInicio;
    @Column(name = "DATA_CONCLUSAO")
    private LocalDate dataConclusao;

    @Column(precision = 4, scale = 2)
    private BigDecimal notaFinal;
}
