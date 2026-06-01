package com.fiap.areslife.dto.request;

import com.fiap.areslife.enums.PacoteViagem;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ViagemRequest(
        @NotNull Long habitanteId,
        @NotNull Long coloniaId,
        @NotNull LocalDate dataPartida,
        @NotNull LocalDate dataRetorno,
        @NotNull PacoteViagem pacote
) {}
