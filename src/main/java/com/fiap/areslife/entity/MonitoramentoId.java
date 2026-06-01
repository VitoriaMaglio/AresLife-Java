package com.fiap.areslife.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoramentoId implements Serializable {

    private Long idRecurso;
    private Long idColonia;
    private LocalDateTime dataRegistro;
}
