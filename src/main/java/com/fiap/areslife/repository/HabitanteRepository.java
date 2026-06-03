package com.fiap.areslife.repository;

import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.enums.StatusHabitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitanteRepository extends JpaRepository<Habitante, Long> {
    List<Habitante> findByColoniaId(Long coloniaId);

    long countByColoniaIdAndStatus(Long coloniaId, StatusHabitante status);
    List<Habitante> findByColoniaIdAndStatus(Long coloniaId, StatusHabitante status);
}
