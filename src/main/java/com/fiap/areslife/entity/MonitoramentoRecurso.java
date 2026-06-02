package com.fiap.areslife.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitoramento_recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoramentoRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_monitoramento")
    @SequenceGenerator(
            name = "seq_monitoramento",
            sequenceName = "SEQ_MONITORAMENTO",
            allocationSize = 1
    )
    @Column(name = "ID_MONITORAMENTO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RECURSO", nullable = false)
    private Recurso recurso;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLONIA")
    private Colonia colonia;

    @Column(name = "DATA_REGISTRO")
    private LocalDateTime dataRegistro;

    @Column(name = "VALOR_REGISTRADO",nullable = false, precision = 10, scale = 2)
    private BigDecimal valorRegistrado;

    @Column(nullable = false)
    private String status;

    @Column(length = 500)
    private String observacao;
}
