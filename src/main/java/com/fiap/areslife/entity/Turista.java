package com.fiap.areslife.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("TURISTA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turista extends Habitante {

    @Column(name = "agencia_turismo")
    private String agenciaTurismo;

    @Column(name = "pacote_selecionado")
    private String pacoteSelecionado;
}
