package com.fiap.areslife.entity;

import com.fiap.areslife.enums.TipoRecurso;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colonia_id", nullable = false)
    private Colonia colonia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecurso tipoRecurso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private String unidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal nivelCritico;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal nivelMaximo;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MonitoramentoRecurso> monitoramentos;
}
