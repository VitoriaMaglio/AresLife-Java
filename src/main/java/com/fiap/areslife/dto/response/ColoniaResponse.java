package com.fiap.areslife.dto.response;

import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusColonia;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class ColoniaResponse extends RepresentationModel<ColoniaResponse> {

    private Long id;
    private String nome;
    private Localizacao localizacao;
    private Integer capacidadeMax;
    private StatusColonia status;
    private LocalDate dataFundacao;
    private String descricao;
    private long totalHabitantes;

    public ColoniaResponse() {}

    public ColoniaResponse(Long id, String nome, Localizacao localizacao, Integer capacidadeMax,
                           StatusColonia status, LocalDate dataFundacao, String descricao, long totalHabitantes) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidadeMax = capacidadeMax;
        this.status = status;
        this.dataFundacao = dataFundacao;
        this.descricao = descricao;
        this.totalHabitantes = totalHabitantes;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Localizacao getLocalizacao() { return localizacao; }
    public Integer getCapacidadeMax() { return capacidadeMax; }
    public StatusColonia getStatus() { return status; }
    public LocalDate getDataFundacao() { return dataFundacao; }
    public String getDescricao() { return descricao; }
    public long getTotalHabitantes() { return totalHabitantes; }
}
