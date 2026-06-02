package com.fiap.areslife.entity;

import com.fiap.areslife.enums.PacoteViagem;
import com.fiap.areslife.enums.StatusViagem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "viagens_turisticas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemTuristica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_viagem")
    @SequenceGenerator(
            name = "seq_viagem",
            sequenceName = "SEQ_VIAGEM",
            allocationSize = 1
    )
    @Column(name = "ID_VIAGEM")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HABITANTE", nullable = false)
    private Habitante habitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLONIA", nullable = false)
    private Colonia colonia;

    @Column(name = "DATA_PARTIDA",nullable = false)
    private LocalDate dataPartida;

    @Column(name = "DATA_RETORNO",nullable = false)
    private LocalDate dataRetorno;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_VIAGEM",nullable = false)
    private StatusViagem statusViagem;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PacoteViagem pacote;
}
