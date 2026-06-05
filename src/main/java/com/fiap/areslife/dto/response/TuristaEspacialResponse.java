package com.fiap.areslife.dto.response;

import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class TuristaEspacialResponse extends RepresentationModel<TuristaEspacialResponse> {

    private Long id;

    // Campos em português (API interna)
    private String nome;
    private Integer idade;
    private String pais;
    private Localizacao destino;
    private StatusTurista status;
    private LocalDate dataCadastro;

    // Campos extras mapeados para o front-end mobile
    // (o front usa: name, age, origin, destination, nationality, missionType,
    //  healthStatus, ticketStatus, oxygenLevel, heartRate, missionDays)
    private String name;
    private Integer age;
    private String origin;
    private String destination;
    private String nationality;
    private String missionType;
    private String healthStatus;
    private String ticketStatus;
    private Integer oxygenLevel;
    private Integer heartRate;
    private Integer missionDays;

    public TuristaEspacialResponse() {}

    public TuristaEspacialResponse(
            Long id,
            String nome,
            Integer idade,
            String pais,
            Localizacao destino,
            StatusTurista status,
            LocalDate dataCadastro) {

        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.pais = pais;
        this.destino = destino;
        this.status = status;
        this.dataCadastro = dataCadastro;

        // Mapeamento para campos que o front-end mobile espera
        this.name        = nome;
        this.age         = idade;
        this.origin      = "Terra";
        this.destination = destino != null ? destino.name().charAt(0) + destino.name().substring(1).toLowerCase() : "";
        this.nationality = pais;
        this.missionType = "Turismo espacial";
        this.healthStatus = mapHealthStatus(status);
        this.ticketStatus = mapTicketStatus(status);
        this.oxygenLevel  = 95;
        this.heartRate    = 80;
        this.missionDays  = 0;
    }

    // Converte StatusTurista → healthStatus legível pelo front
    private String mapHealthStatus(StatusTurista s) {
        if (s == null) return "Estável";
        return switch (s) {
            case AGUARDANDO, EMBARCADO -> "Estável";
            case EM_TRANSITO -> "Acompanhamento";
            case CHEGOU -> "Estável";
            case CANCELADO -> "Inativo";
        };
    }

    // Converte StatusTurista → ticketStatus legível pelo front
    private String mapTicketStatus(StatusTurista s) {
        if (s == null) return "Pendente";
        return switch (s) {
            case AGUARDANDO -> "Pendente";
            case EMBARCADO, EM_TRANSITO, CHEGOU -> "Confirmado";
            case CANCELADO -> "Cancelado";
        };
    }

    // ---- Getters ----

    public Long getId()             { return id; }
    public String getNome()         { return nome; }
    public Integer getIdade()       { return idade; }
    public String getPais()         { return pais; }
    public Localizacao getDestino() { return destino; }
    public StatusTurista getStatus(){ return status; }
    public LocalDate getDataCadastro() { return dataCadastro; }

    // Getters para o front-end mobile
    public String getName()        { return name; }
    public Integer getAge()        { return age; }
    public String getOrigin()      { return origin; }
    public String getDestination() { return destination; }
    public String getNationality() { return nationality; }
    public String getMissionType() { return missionType; }
    public String getHealthStatus(){ return healthStatus; }
    public String getTicketStatus(){ return ticketStatus; }
    public Integer getOxygenLevel(){ return oxygenLevel; }
    public Integer getHeartRate()  { return heartRate; }
    public Integer getMissionDays(){ return missionDays; }
}
