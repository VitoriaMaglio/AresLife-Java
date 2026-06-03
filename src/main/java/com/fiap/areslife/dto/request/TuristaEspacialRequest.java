package com.fiap.areslife.dto.request;

import com.fiap.areslife.enums.Localizacao;
import jakarta.validation.constraints.*;

public record TuristaEspacialRequest(
        @NotBlank String nome,
        @NotNull @Min(18) @Max(99) Integer idade,
        @NotBlank String pais,
        @NotNull Localizacao destino
) {}
