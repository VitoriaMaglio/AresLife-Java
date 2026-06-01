package com.fiap.areslife.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "monitoramento_recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoramentoRecurso {

    // Modelagem avançada: @EmbeddedId (chave composta)
    @EmbeddedId
    private MonitoramentoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRecurso")
    @JoinColumn(name = "id_recurso")
    private Recurso recurso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorRegistrado;

    @Column(nullable = false)
    private String status;

    @Column(length = 500)
    private String observacao;
}
