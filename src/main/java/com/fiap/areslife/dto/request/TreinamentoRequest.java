package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TreinamentoRequest(
        @NotNull Long habitanteId,
        @NotBlank String tipoTreinamento,
        @NotNull @Min(1) Integer cargaHoraria
) {}
