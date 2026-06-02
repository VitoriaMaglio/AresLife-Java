package com.fiap.areslife.repository;

import com.fiap.areslife.entity.SaudeHabitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaudeHabitanteRepository extends JpaRepository<SaudeHabitante, Long> {
    Page<SaudeHabitante> findByHabitanteIdOrderByDataRegistroDesc(Long habitanteId, Pageable pageable);
    Optional<SaudeHabitante> findTopByHabitanteIdOrderByDataRegistroDesc(Long habitanteId);
}
