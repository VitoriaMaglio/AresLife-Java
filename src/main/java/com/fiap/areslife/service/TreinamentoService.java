package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.TreinamentoRequest;
import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.entity.Treinamento;
import com.fiap.areslife.enums.StatusTreinamento;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.TreinamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreinamentoService {

    private final TreinamentoRepository treinamentoRepository;
    private final HabitanteRepository habitanteRepository;
    private final LogSistemaService logService;

    public List<Treinamento> listar(Long habitanteId, StatusTreinamento status) {
        if (habitanteId != null && status != null)
            return treinamentoRepository.findByHabitanteIdAndStatus(habitanteId, status);
        if (habitanteId != null)
            return treinamentoRepository.findByHabitanteId(habitanteId);
        return treinamentoRepository.findAll();
    }

    public Treinamento buscarPorId(Long id) {
        return treinamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treinamento não encontrado com id: " + id));
    }

    @Transactional
    public Treinamento criar(TreinamentoRequest request) {
        Habitante habitante = habitanteRepository.findById(request.habitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Habitante não encontrado com id: " + request.habitanteId()));
        Treinamento treinamento = Treinamento.builder()
                .habitante(habitante)
                .tipoTreinamento(request.tipoTreinamento())
                .cargaHoraria(request.cargaHoraria())
                .status(StatusTreinamento.PENDENTE)
                .build();
        Treinamento salvo = treinamentoRepository.save(treinamento);
        logService.registrar("INSERT", "treinamentos",
                "Treinamento criado: '" + salvo.getTipoTreinamento() + "' (id=" + salvo.getId() + ") para habitante id=" + request.habitanteId());
        return salvo;
    }

    @Transactional
    public Treinamento iniciar(Long id) {
        Treinamento t = buscarPorId(id);
        if (t.getStatus() == StatusTreinamento.CONCLUIDO)
            throw new BusinessException("Treinamento já foi concluído e não pode ser reiniciado.");
        if (t.getStatus() == StatusTreinamento.EM_ANDAMENTO)
            throw new BusinessException("Treinamento já está em andamento.");
        t.setStatus(StatusTreinamento.EM_ANDAMENTO);
        t.setDataInicio(LocalDate.now());
        Treinamento salvo = treinamentoRepository.save(t);
        logService.registrar("UPDATE", "treinamentos",
                "Treinamento id=" + id + " iniciado: '" + salvo.getTipoTreinamento() + "'");
        return salvo;
    }

    @Transactional
    public Treinamento concluir(Long id, BigDecimal notaFinal) {
        Treinamento t = buscarPorId(id);
        if (t.getStatus() == StatusTreinamento.CONCLUIDO)
            throw new BusinessException("Treinamento já foi concluído.");
        if (t.getStatus() == StatusTreinamento.PENDENTE)
            throw new BusinessException("Treinamento ainda não foi iniciado.");
        if (notaFinal.compareTo(BigDecimal.ZERO) < 0 || notaFinal.compareTo(new BigDecimal("10")) > 0)
            throw new BusinessException("Nota final deve ser entre 0 e 10.");
        t.setStatus(StatusTreinamento.CONCLUIDO);
        t.setDataConclusao(LocalDate.now());
        t.setNotaFinal(notaFinal);
        Treinamento salvo = treinamentoRepository.save(t);
        logService.registrar("UPDATE", "treinamentos",
                "Treinamento id=" + id + " concluído com nota " + notaFinal);
        return salvo;
    }

    public List<Treinamento> listarPendentesPorColonia(Long coloniaId) {
        return treinamentoRepository.findPendentesByColonia(coloniaId);
    }

    @Transactional
    public void deletar(Long id) {
        Treinamento t = buscarPorId(id);
        String tipo = t.getTipoTreinamento();
        treinamentoRepository.deleteById(id);
        logService.registrar("DELETE", "treinamentos",
                "Treinamento removido: '" + tipo + "' (id=" + id + ")");
    }
}
