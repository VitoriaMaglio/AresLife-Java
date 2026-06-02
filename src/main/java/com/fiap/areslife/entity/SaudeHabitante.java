package com.fiap.areslife.entity;

import com.fiap.areslife.enums.StatusSaude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "saude_habitantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaudeHabitante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_saude")
    @SequenceGenerator(
            name = "seq_saude",
            sequenceName = "SEQ_SAUDE",
            allocationSize = 1
    )
    @Column(name = "ID_SAUDE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HABITANTE", nullable = false)
    private Habitante habitante;

    // Modelagem avançada: @Embedded
    @Embedded
    private SinaisVitais sinaisVitais;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_SAUDE",nullable = false)
    private StatusSaude statusSaude;

    @Column(name = "DATA_REGISTRO",nullable = false)
    private LocalDateTime dataRegistro;

    @Column(length = 500)
    private String observacoes;
}
