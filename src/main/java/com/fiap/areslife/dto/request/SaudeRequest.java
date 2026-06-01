package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SaudeRequest(
        @NotBlank String pressaoArterial,
        @NotNull @Min(40) @Max(200) Integer frequenciaCardiaca,
        @NotNull @DecimalMin("70.0") @DecimalMax("100.0") BigDecimal saturacaoOxigenio,
        @NotNull @DecimalMin("35.0") @DecimalMax("42.0") BigDecimal temperaturaCorporal,
        String observacoes
) {}
