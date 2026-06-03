package com.fiap.areslife.service.mapper;

import com.fiap.areslife.dto.response.HabitanteResponse;
import com.fiap.areslife.entity.Habitante;

public class HabitanteMapper {

    public static HabitanteResponse toResponse(Habitante h) {
        return new HabitanteResponse(
                h.getId(),
                h.getColonia() != null ? h.getColonia().getId() : null,
                h.getNome(),
                h.getNacionalidade(),
                h.getDataChegada(),
                h.getDataSaida(),
                h.getStatus(),
                h.getClass().getSimpleName()
        );
    }
}