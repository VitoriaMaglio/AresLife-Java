package com.fiap.areslife.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Tabela: LOGS_SISTEMA
 *
 * Tabela existente no banco mas SEM entidade no projeto — CRIADA AGORA.
 *
 * Colunas do banco:
 *  id_log          NUMBER  PK
 *  operacao        VARCHAR2(100)  NOT NULL
 *  tabela_afetada  VARCHAR2(50)
 *  descricao       VARCHAR2(500)
 *  usuario         VARCHAR2(50)  DEFAULT USER
 *  data_operacao   TIMESTAMP     DEFAULT SYSTIMESTAMP
 */
@Entity
@Table(name = "logs_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log")
    @SequenceGenerator(name = "seq_log", sequenceName = "SEQ_LOG", allocationSize = 1)
    @Column(name = "id_log")
    private Long id;


    @Column(name = "operacao", nullable = false, length = 100)
    private String operacao;


    @Column(name = "tabela_afetada", length = 50)
    private String tabelaAfetada;


    @Column(name = "descricao", length = 500)
    private String descricao;


    @Column(name = "usuario", length = 50)
    private String usuario;


    @Column(name = "data_operacao", nullable = false)
    private LocalDateTime dataOperacao;
}
