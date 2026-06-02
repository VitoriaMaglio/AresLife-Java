package com.fiap.areslife.repository;

import com.fiap.areslife.entity.Recurso;
import com.fiap.areslife.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByColoniaId(Long coloniaId);
    Optional<Recurso> findByColoniaIdAndTipoRecurso(Long coloniaId, TipoRecurso tipo);
    boolean existsByColoniaIdAndTipoRecurso(Long coloniaId, TipoRecurso tipo);
}
