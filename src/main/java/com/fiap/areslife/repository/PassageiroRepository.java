package com.fiap.areslife.repository;

import com.fiap.areslife.entity.Passageiro;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusPassageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
    List<Passageiro> findByStatus(StatusPassageiro status);
    List<Passageiro> findByDestino(Localizacao destino);
    List<Passageiro> findByDestinoAndStatus(Localizacao destino, StatusPassageiro status);
}
