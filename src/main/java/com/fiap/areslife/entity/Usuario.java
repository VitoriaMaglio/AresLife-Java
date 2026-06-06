package com.fiap.areslife.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(
            name = "seq_usuario",
            sequenceName = "SEQ_USUARIO",
            allocationSize = 1
    )
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOME", length = 100)
    private String nome;

    @Column(name = "EMAIL", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "SENHA", length = 255, nullable = false)
    private String senha;

    @Column(name = "ROLE", length = 20)
    private String role;

}
