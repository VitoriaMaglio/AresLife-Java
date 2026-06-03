package com.fiap.areslife.dto.response;

import com.fiap.areslife.enums.StatusHabitante;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class AstronautaResponse extends RepresentationModel<AstronautaResponse> {

    private Long id;
    private Long coloniaId;
    private String nome;
    private String nacionalidade;
    private LocalDate dataChegada;
    private LocalDate dataSaida;
    private StatusHabitante status;
    private String especialidade;
    private String missaoAtual;

    public AstronautaResponse() {}

    public AstronautaResponse(
            Long id,
            Long coloniaId,
            String nome,
            String nacionalidade,
            LocalDate dataChegada,
            LocalDate dataSaida,
            StatusHabitante status,
            String especialidade,
            String missaoAtual) {

        this.id = id;
        this.coloniaId = coloniaId;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.status = status;
        this.especialidade = especialidade;
        this.missaoAtual = missaoAtual;
    }

    public Long getId() { return id; }
    public Long getColoniaId() { return coloniaId; }
    public String getNome() { return nome; }
    public String getNacionalidade() { return nacionalidade; }
    public LocalDate getDataChegada() { return dataChegada; }
    public LocalDate getDataSaida() { return dataSaida; }
    public StatusHabitante getStatus() { return status; }
    public String getEspecialidade() { return especialidade; }
    public String getMissaoAtual() { return missaoAtual; }
}