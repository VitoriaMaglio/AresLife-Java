package com.fiap.areslife.dto.request;

import com.fiap.areslife.enums.TipoRecurso;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RecursoRequest(
        @NotNull Long coloniaId,
        @NotNull TipoRecurso tipoRecurso,
        @NotNull @DecimalMin("0.0") BigDecimal quantidade,
        @NotBlank String unidade,
        @NotNull @DecimalMin("0.0") BigDecimal nivelCritico,
        @NotNull @DecimalMin("0.0") BigDecimal nivelMaximo
) {}
