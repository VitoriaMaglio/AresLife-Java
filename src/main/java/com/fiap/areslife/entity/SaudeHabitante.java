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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitante_id", nullable = false)
    private Habitante habitante;

    // Modelagem avançada: @Embedded
    @Embedded
    private SinaisVitais sinaisVitais;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSaude statusSaude;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column(length = 500)
    private String observacoes;
}
