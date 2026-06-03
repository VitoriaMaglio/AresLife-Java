package com.fiap.areslife.dto.response;

import com.fiap.areslife.enums.TipoRecurso;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecursoResponse extends RepresentationModel<RecursoResponse> {

    private Long id;
    private Long coloniaId;
    private TipoRecurso tipoRecurso;
    private BigDecimal quantidade;
    private String unidade;
    private BigDecimal nivelCritico;
    private BigDecimal nivelMaximo;
    private LocalDate dataAtualizacao;

    public RecursoResponse() {}

    public RecursoResponse(
            Long id,
            Long coloniaId,
            TipoRecurso tipoRecurso,
            BigDecimal quantidade,
            String unidade,
            BigDecimal nivelCritico,
            BigDecimal nivelMaximo,
            LocalDate dataAtualizacao) {

        this.id = id;
        this.coloniaId = coloniaId;
        this.tipoRecurso = tipoRecurso;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.nivelCritico = nivelCritico;
        this.nivelMaximo = nivelMaximo;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() { return id; }
    public Long getColoniaId() { return coloniaId; }
    public TipoRecurso getTipoRecurso() { return tipoRecurso; }
    public BigDecimal getQuantidade() { return quantidade; }
    public String getUnidade() { return unidade; }
    public BigDecimal getNivelCritico() { return nivelCritico; }
    public BigDecimal getNivelMaximo() { return nivelMaximo; }
    public LocalDate getDataAtualizacao() { return dataAtualizacao; }
}