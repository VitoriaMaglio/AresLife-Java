package com.fiap.areslife.dto.request;

import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import jakarta.validation.constraints.*;


public record TuristaEspacialRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Idade é obrigatória")
        @Min(value = 18, message = "Idade mínima é 18 anos.")
        @Max(value = 99, message = "Idade máxima é 99 anos.")
        Integer idade,

        @NotBlank(message = "País é obrigatório")
        String pais,

        @NotNull(message = "Destino é obrigatório (MARTE ou LUA)")
        Localizacao destino,

        StatusTurista status
) {}
