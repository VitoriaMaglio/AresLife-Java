package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record HabitanteRequest(
        @NotNull Long coloniaId,
        @NotBlank String nome,
        @NotBlank String nacionalidade,
        @NotNull LocalDate dataChegada,
        // Astronauta fields (optional)
        String especialidade,
        String missaoAtual,
        // Turista fields (optional)
        String agenciaTurismo,
        String pacoteSelecionado
) {}
