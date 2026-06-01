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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitante_id", nullable = false)
    private Habitante habitante;

    @Column(nullable = false)
    private String tipoTreinamento;

    @Column(nullable = false)
    private Integer cargaHoraria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTreinamento status;

    private LocalDate dataInicio;

    private LocalDate dataConclusao;

    @Column(precision = 4, scale = 2)
    private BigDecimal notaFinal;
}
