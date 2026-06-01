package com.fiap.areslife.dto.request;

import com.fiap.areslife.enums.Localizacao;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ColoniaRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Localização é obrigatória")
        Localizacao localizacao,

        @NotNull(message = "Capacidade máxima é obrigatória")
        @Min(value = 1, message = "Capacidade mínima é 1")
        Integer capacidadeMax,

        @NotNull(message = "Data de fundação é obrigatória")
        LocalDate dataFundacao,

        String descricao
) {}
