package com.fiap.areslife.dto.response;

import com.fiap.areslife.enums.StatusHabitante;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class TuristaResponse extends RepresentationModel<TuristaResponse> {

    private Long id;
    private Long coloniaId;
    private String nome;
    private String nacionalidade;
    private LocalDate dataChegada;
    private LocalDate dataSaida;
    private StatusHabitante status;
    private String agenciaTurismo;
    private String pacoteSelecionado;

    public TuristaResponse() {}

    public TuristaResponse(
            Long id,
            Long coloniaId,
            String nome,
            String nacionalidade,
            LocalDate dataChegada,
            LocalDate dataSaida,
            StatusHabitante status,
            String agenciaTurismo,
            String pacoteSelecionado) {

        this.id = id;
        this.coloniaId = coloniaId;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.status = status;
        this.agenciaTurismo = agenciaTurismo;
        this.pacoteSelecionado = pacoteSelecionado;
    }

    public Long getId() { return id; }
    public Long getColoniaId() { return coloniaId; }
    public String getNome() { return nome; }
    public String getNacionalidade() { return nacionalidade; }
    public LocalDate getDataChegada() { return dataChegada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public StatusHabitante getStatus() { return status; }
    public String getAgenciaTurismo() { return agenciaTurismo; }
    public String getPacoteSelecionado() { return pacoteSelecionado; }
}