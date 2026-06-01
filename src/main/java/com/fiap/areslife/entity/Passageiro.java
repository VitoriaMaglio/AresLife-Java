package com.fiap.areslife.entity;

import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusPassageiro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passageiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String pais;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Localizacao destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPassageiro status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitante_id")
    private Habitante habitante;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;
}
