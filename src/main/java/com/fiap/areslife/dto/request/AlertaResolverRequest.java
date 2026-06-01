package com.fiap.areslife.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AlertaResolverRequest(
        @NotBlank(message = "Observação é obrigatória") String observacao
) {}
