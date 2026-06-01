package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AbastecerRequest(
        @NotNull @DecimalMin("0.01") BigDecimal quantidade
) {}
