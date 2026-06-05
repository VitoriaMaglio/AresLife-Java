package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.SaudeRequest;
import com.fiap.areslife.entity.*;
import com.fiap.areslife.enums.SeveridadeAlerta;
import com.fiap.areslife.enums.StatusAlerta;
import com.fiap.areslife.enums.StatusSaude;
import com.fiap.areslife.enums.TipoAlerta;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.AlertaRepository;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.SaudeHabitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaudeService {

    private final SaudeHabitanteRepository saudeRepository;
    private final HabitanteRepository habitanteRepository;
    private final AlertaRepository alertaRepository;
    private final LogSistemaService logService;

    @Transactional
    public SaudeHabitante criar(Long habitanteId, SaudeRequest request) {
        Habitante habitante = habitanteRepository.findById(habitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitante não encontrado com id: " + habitanteId));

        SinaisVitais sinais = SinaisVitais.builder()
                .pressaoArterial(request.pressaoArterial())
                .frequenciaCardiaca(request.frequenciaCardiaca())
                .saturacaoOxigenio(request.saturacaoOxigenio())
                .temperaturaCorporal(request.temperaturaCorporal())
                .build();

        StatusSaude statusCalculado = calcularStatus(request);

        SaudeHabitante saude = SaudeHabitante.builder()
                .habitante(habitante)
                .sinaisVitais(sinais)
                .statusSaude(statusCalculado)
                .dataRegistro(LocalDateTime.now())
                .observacoes(request.observacoes())
                .build();

        SaudeHabitante salvo = saudeRepository.save(saude);
        logService.registrar("INSERT", "saude_habitantes",
                "Registro de saúde criado (id=" + salvo.getId() + ") para habitante id=" + habitanteId + ", status=" + statusCalculado);

        if (statusCalculado == StatusSaude.CRITICO) {
            criarAlertaSaude(habitante);
        }
        return salvo;
    }

    public Page<SaudeHabitante> historico(Long habitanteId, Pageable pageable) {
        if (!habitanteRepository.existsById(habitanteId))
            throw new ResourceNotFoundException("Habitante não encontrado com id: " + habitanteId);
        return saudeRepository.findByHabitanteIdOrderByDataRegistroDesc(habitanteId, pageable);
    }

    public SaudeHabitante ultimoRegistro(Long habitanteId) {
        return saudeRepository.findTopByHabitanteIdOrderByDataRegistroDesc(habitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum registro de saúde para o habitante " + habitanteId));
    }

    @Transactional(readOnly = true)
    public List<SaudeHabitante> listar(Long habitanteId) {
        if (!habitanteRepository.existsById(habitanteId))
            throw new ResourceNotFoundException("Habitante não encontrado com id: " + habitanteId);
        return saudeRepository.findByHabitanteId(habitanteId);
    }

    @Transactional(readOnly = true)
    public SaudeHabitante buscarPorId(Long id) {
        return saudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de saúde não encontrado com id: " + id));
    }

    @Transactional
    public SaudeHabitante atualizar(Long id, SaudeRequest request) {
        SaudeHabitante saude = buscarPorId(id);
        saude.getSinaisVitais().setPressaoArterial(request.pressaoArterial());
        saude.getSinaisVitais().setFrequenciaCardiaca(request.frequenciaCardiaca());
        saude.getSinaisVitais().setSaturacaoOxigenio(request.saturacaoOxigenio());
        saude.getSinaisVitais().setTemperaturaCorporal(request.temperaturaCorporal());
        saude.setObservacoes(request.observacoes());
        saude.setStatusSaude(calcularStatus(request));
        SaudeHabitante salvo = saudeRepository.save(saude);
        logService.registrar("UPDATE", "saude_habitantes",
                "Registro de saúde id=" + id + " atualizado, novo status=" + salvo.getStatusSaude());
        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        if (!saudeRepository.existsById(id))
            throw new ResourceNotFoundException("Registro de saúde não encontrado com id: " + id);
        saudeRepository.deleteById(id);
        logService.registrar("DELETE", "saude_habitantes", "Registro de saúde removido (id=" + id + ")");
    }

    private StatusSaude calcularStatus(SaudeRequest req) {
        BigDecimal sat  = req.saturacaoOxigenio();
        BigDecimal temp = req.temperaturaCorporal();
        int freq = req.frequenciaCardiaca();
        if (sat.compareTo(new BigDecimal("90")) < 0 || temp.compareTo(new BigDecimal("39.5")) > 0 || freq > 130)
            return StatusSaude.CRITICO;
        if (sat.compareTo(new BigDecimal("95")) < 0 || temp.compareTo(new BigDecimal("38.0")) > 0 || freq > 100)
            return StatusSaude.ATENCAO;
        return StatusSaude.NORMAL;
    }

    private void criarAlertaSaude(Habitante habitante) {
        alertaRepository.save(Alerta.builder()
                .colonia(habitante.getColonia())
                .tipoAlerta(TipoAlerta.SAUDE)
                .descricao("Habitante " + habitante.getNome() + " apresenta sinais vitais CRÍTICOS.")
                .severidade(SeveridadeAlerta.CRITICA)
                .dataEmissao(LocalDateTime.now())
                .status(StatusAlerta.ABERTO)
                .build());
        logService.registrar("INSERT", "alertas",
                "Alerta de saúde crítica gerado para habitante: " + habitante.getNome());
    }
}
