package com.fiap.areslife.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinaisVitais {
    @Column(name = "PRESSAO_ARTERIAL")
    private String pressaoArterial;
    @Column(name = "FREQUENCIA_CARDIACA")
    private Integer frequenciaCardiaca;
    @Column(name = "SATURACAO_OXIGENIO")
    private BigDecimal saturacaoOxigenio;
    @Column(name = "TEMPERATURA_CORPORAL")
    private BigDecimal temperaturaCorporal;
}
