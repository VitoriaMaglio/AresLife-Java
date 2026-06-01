package com.fiap.areslife.entity;

import com.fiap.areslife.enums.SeveridadeAlerta;
import com.fiap.areslife.enums.StatusAlerta;
import com.fiap.areslife.enums.TipoAlerta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colonia_id", nullable = false)
    private Colonia colonia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipoAlerta;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeveridadeAlerta severidade;

    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAlerta status;

    private LocalDateTime resolvidoEm;
}
