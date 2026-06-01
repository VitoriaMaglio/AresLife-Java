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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitante_id", nullable = false)
    private Habitante habitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colonia_id", nullable = false)
    private Colonia colonia;

    @Column(nullable = false)
    private LocalDate dataPartida;

    @Column(nullable = false)
    private LocalDate dataRetorno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusViagem statusViagem;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PacoteViagem pacote;
}
