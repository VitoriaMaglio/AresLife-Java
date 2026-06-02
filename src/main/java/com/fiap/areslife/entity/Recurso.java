package com.fiap.areslife.entity;

import com.fiap.areslife.enums.TipoRecurso;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recurso")
    @SequenceGenerator(
            name = "seq_recurso",
            sequenceName = "SEQ_RECURSO",
            allocationSize = 1
    )
    @Column(name = "ID_RECURSO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLONIA", nullable = false)
    private Colonia colonia;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_RECURSO",nullable = false)
    private TipoRecurso tipoRecurso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "NIVEL_CRITICO",nullable = false, precision = 10, scale = 2)
    private BigDecimal nivelCritico;

    @Column(name = "NIVEL_MAXIMO",nullable = false, precision = 10, scale = 2)
    private BigDecimal nivelMaximo;

    @Column(name = "DATA_ATUALIZACAO",nullable = false)
    private LocalDate dataAtualizacao;

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MonitoramentoRecurso> monitoramentos;
}
