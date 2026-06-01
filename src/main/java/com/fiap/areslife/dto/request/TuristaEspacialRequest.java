package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.*;

public record TuristaEspacialRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Idade é obrigatória")
        @Min(value = 18, message = "Idade mínima é 18 anos")
        @Max(value = 99, message = "Idade máxima é 99 anos")
        Integer idade,

        @NotBlank(message = "País é obrigatório")
        String pais,

        @NotBlank(message = "Destino é obrigatório")
        @Pattern(regexp = "Marte|Lua", message = "Destino deve ser 'Marte' ou 'Lua'")
        String destino,

        @NotBlank(message = "Status é obrigatório")
        String status
) {}
