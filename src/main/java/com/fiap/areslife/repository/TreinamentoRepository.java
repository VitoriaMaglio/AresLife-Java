package com.fiap.areslife.repository;

import com.fiap.areslife.entity.Treinamento;
import com.fiap.areslife.enums.StatusTreinamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreinamentoRepository extends JpaRepository<Treinamento, Long> {
    List<Treinamento> findByHabitanteId(Long habitanteId);
    List<Treinamento> findByHabitanteIdAndStatus(Long habitanteId, StatusTreinamento status);
    boolean existsByHabitanteIdAndStatus(Long habitanteId, StatusTreinamento status);

    @Query("SELECT t FROM Treinamento t WHERE t.habitante.colonia.id = :coloniaId AND t.status IN ('PENDENTE', 'EM_ANDAMENTO')")
    List<Treinamento> findPendentesByColonia(Long coloniaId);
}
