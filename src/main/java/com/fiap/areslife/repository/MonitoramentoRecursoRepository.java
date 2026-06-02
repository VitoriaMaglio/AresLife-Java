package com.fiap.areslife.repository;

import com.fiap.areslife.entity.MonitoramentoId;
import com.fiap.areslife.entity.MonitoramentoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoramentoRecursoRepository extends JpaRepository<MonitoramentoRecurso, MonitoramentoId> {
    List<MonitoramentoRecurso> findByIdIdRecursoOrderByIdDataRegistroDesc(Long recursoId);
}
