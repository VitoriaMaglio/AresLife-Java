package com.fiap.areslife.service.mapper;

import com.fiap.areslife.dto.response.TuristaEspacialResponse;
import com.fiap.areslife.entity.TuristaEspacial;

public class TuristaEspacialMapper {

    public static TuristaEspacialResponse toResponse(TuristaEspacial turista) {
        return new TuristaEspacialResponse(
                turista.getId(),
                turista.getNome(),
                turista.getIdade(),
                turista.getPais(),
                turista.getDestino(),
                turista.getStatus(),
                turista.getDataCadastro()
        );
    }
}
