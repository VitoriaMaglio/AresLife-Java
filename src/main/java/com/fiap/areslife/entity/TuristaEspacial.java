package com.fiap.areslife.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "turistas_espaciais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuristaEspacial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private String status;
}
