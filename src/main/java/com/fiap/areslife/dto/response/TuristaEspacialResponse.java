package com.fiap.areslife.dto.response;

import org.springframework.hateoas.RepresentationModel;

public class TuristaEspacialResponse extends RepresentationModel<TuristaEspacialResponse> {

    private Long id;
    private String nome;
    private Integer idade;
    private String pais;
    private String destino;
    private String status;

    public TuristaEspacialResponse() {}

    public TuristaEspacialResponse(Long id, String nome, Integer idade, String pais, String destino, String status) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.pais = pais;
        this.destino = destino;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getIdade() { return idade; }
    public String getPais() { return pais; }
    public String getDestino() { return destino; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public void setPais(String pais) { this.pais = pais; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setStatus(String status) { this.status = status; }
}
