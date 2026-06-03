package com.fiap.areslife.service.mapper;

import com.fiap.areslife.dto.response.ColoniaResponse;
import com.fiap.areslife.entity.Colonia;

public class ColoniaMapper {

    public static ColoniaResponse toResponse(Colonia c, long totalHabitantes) {
        return new ColoniaResponse(
                c.getId(),
                c.getNome(),
                c.getLocalizacao(),
                c.getCapacidadeMax(),
                c.getStatus(),
                c.getDataFundacao(),
                c.getDescricao(),
                totalHabitantes
        );
    }
}