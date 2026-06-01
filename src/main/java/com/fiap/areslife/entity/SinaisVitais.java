package com.fiap.areslife.entity;

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

    private String pressaoArterial;

    private Integer frequenciaCardiaca;

    private BigDecimal saturacaoOxigenio;

    private BigDecimal temperaturaCorporal;
}
