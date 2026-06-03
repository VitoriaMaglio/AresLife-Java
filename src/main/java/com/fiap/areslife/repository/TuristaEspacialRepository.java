package com.fiap.areslife.repository;

import com.fiap.areslife.entity.TuristaEspacial;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuristaEspacialRepository extends JpaRepository<TuristaEspacial, Long> {
    List<TuristaEspacial> findByDestino(String destino);
    List<TuristaEspacial> findByStatus(String status);
    List<TuristaEspacial> findByPais(String pais);
    List<TuristaEspacial> findByStatus(StatusTurista status);
    List<TuristaEspacial> findByDestino(Localizacao destino);
    List<TuristaEspacial> findByDestinoAndStatus(Localizacao destino, StatusTurista status);
}

