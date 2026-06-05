package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.AbastecerRequest;
import com.fiap.areslife.dto.request.RecursoRequest;
import com.fiap.areslife.entity.*;
import com.fiap.areslife.enums.*;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.AlertaRepository;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.MonitoramentoRecursoRepository;
import com.fiap.areslife.repository.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final ColoniaService coloniaService;
    private final HabitanteRepository habitanteRepository;
    private final AlertaRepository alertaRepository;
    private final MonitoramentoRecursoRepository monitoramentoRepository;
    private final LogSistemaService logService;

    public List<Recurso> listarPorColonia(Long coloniaId) {
        coloniaService.findOrThrow(coloniaId);
        return recursoRepository.findByColoniaId(coloniaId);
    }

    public Recurso buscarPorId(Long id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com id: " + id));
    }

    @Transactional
    public Recurso criar(RecursoRequest request) {
        coloniaService.findOrThrow(request.coloniaId());
        if (recursoRepository.existsByColoniaIdAndTipoRecurso(request.coloniaId(), request.tipoRecurso()))
            throw new BusinessException("Já existe um recurso do tipo " + request.tipoRecurso() + " nesta colônia.");
        if (request.nivelCritico().compareTo(request.nivelMaximo()) >= 0)
            throw new BusinessException("Nível crítico deve ser menor que o nível máximo.");

        Colonia colonia = coloniaService.findOrThrow(request.coloniaId());
        Recurso recurso = Recurso.builder()
                .colonia(colonia)
                .tipoRecurso(request.tipoRecurso())
                .quantidade(request.quantidade())
                .unidade(request.unidade())
                .nivelCritico(request.nivelCritico())
                .nivelMaximo(request.nivelMaximo())
                .dataAtualizacao(LocalDate.now())
                .build();

        Recurso salvo = recursoRepository.save(recurso);
        logService.registrar("INSERT", "recursos",
                "Recurso criado: " + salvo.getTipoRecurso() + " (id=" + salvo.getId() + "), colônia id=" + request.coloniaId());
        return salvo;
    }

    @Transactional
    public Recurso abastecer(Long id, AbastecerRequest request) {
        Recurso recurso = buscarPorId(id);
        BigDecimal anterior = recurso.getQuantidade();
        BigDecimal novaQtd = anterior.add(request.quantidade());
        if (novaQtd.compareTo(recurso.getNivelMaximo()) > 0) novaQtd = recurso.getNivelMaximo();
        recurso.setQuantidade(novaQtd);
        recurso.setDataAtualizacao(LocalDate.now());
        if (novaQtd.compareTo(recurso.getNivelCritico()) > 0) resolverAlertasRecurso(recurso.getColonia().getId());
        registrarMonitoramento(recurso, novaQtd, "NORMAL", "Abastecimento realizado.");
        Recurso salvo = recursoRepository.save(recurso);
        logService.registrar("UPDATE", "recursos",
                "Recurso abastecido: " + recurso.getTipoRecurso() + " id=" + id + " | " + anterior + " → " + novaQtd + " " + recurso.getUnidade());
        return salvo;
    }

    @Transactional
    public void simularDia(Long coloniaId) {
        coloniaService.findOrThrow(coloniaId);
        long numHabitantes = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);
        if (numHabitantes == 0) return;

        for (Recurso recurso : recursoRepository.findByColoniaId(coloniaId)) {
            BigDecimal consumo = calcularConsumoDiario(recurso.getTipoRecurso())
                    .multiply(BigDecimal.valueOf(numHabitantes));
            BigDecimal novaQtd = recurso.getQuantidade().subtract(consumo).max(BigDecimal.ZERO);
            recurso.setQuantidade(novaQtd);
            recurso.setDataAtualizacao(LocalDate.now());
            String statusMon = "NORMAL";
            if (novaQtd.compareTo(recurso.getNivelCritico()) <= 0) {
                statusMon = "CRITICO";
                criarAlertaRecursoCritico(coloniaService.findOrThrow(coloniaId), recurso);
            }
            registrarMonitoramento(recurso, novaQtd, statusMon, "Consumo diário simulado.");
            recursoRepository.save(recurso);
        }
        logService.registrar("UPDATE", "recursos",
                "Simulação de dia executada para colônia id=" + coloniaId + " com " + numHabitantes + " habitante(s)");
    }

    public int calcularAutonomia(Long coloniaId, TipoRecurso tipo) {
        Recurso recurso = recursoRepository.findByColoniaIdAndTipoRecurso(coloniaId, tipo)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso do tipo " + tipo + " não encontrado."));
        long numHabitantes = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);
        if (numHabitantes == 0) return Integer.MAX_VALUE;
        BigDecimal consumoDiario = calcularConsumoDiario(tipo).multiply(BigDecimal.valueOf(numHabitantes));
        if (consumoDiario.compareTo(BigDecimal.ZERO) == 0) return Integer.MAX_VALUE;
        return recurso.getQuantidade().divide(consumoDiario, 0, RoundingMode.FLOOR).intValue();
    }

    @Transactional
    public void deletar(Long id) {
        Recurso recurso = buscarPorId(id);
        String tipo = recurso.getTipoRecurso().name();
        recursoRepository.deleteById(id);
        logService.registrar("DELETE", "recursos", "Recurso removido: " + tipo + " (id=" + id + ")");
    }

    private BigDecimal calcularConsumoDiario(TipoRecurso tipo) {
        return switch (tipo) {
            case OXIGENIO    -> new BigDecimal("0.84");
            case AGUA        -> new BigDecimal("3.00");
            case ENERGIA     -> new BigDecimal("15.00");
            case ALIMENTACAO -> new BigDecimal("0.60");
        };
    }

    private void registrarMonitoramento(Recurso recurso, BigDecimal valor, String status, String obs) {
        monitoramentoRepository.save(MonitoramentoRecurso.builder()
                .recurso(recurso).colonia(recurso.getColonia())
                .valorRegistrado(valor).dataRegistro(LocalDateTime.now())
                .status(status).observacao(obs).build());
    }

    private void criarAlertaRecursoCritico(Colonia colonia, Recurso recurso) {
        alertaRepository.save(Alerta.builder()
                .colonia(colonia).tipoAlerta(TipoAlerta.RECURSO_CRITICO)
                .descricao("Recurso " + recurso.getTipoRecurso() + " atingiu nível crítico: " + recurso.getQuantidade() + " " + recurso.getUnidade())
                .severidade(SeveridadeAlerta.CRITICA).dataEmissao(LocalDateTime.now()).status(StatusAlerta.ABERTO).build());
        logService.registrar("INSERT", "alertas",
                "Alerta de recurso crítico: " + recurso.getTipoRecurso() + " na colônia id=" + colonia.getId());
    }

    private void resolverAlertasRecurso(Long coloniaId) {
        List<Alerta> alertas = alertaRepository.findByColoniaIdAndTipoAlertaAndStatusNot(
                coloniaId, TipoAlerta.RECURSO_CRITICO, StatusAlerta.RESOLVIDO);
        alertas.forEach(a -> { a.setStatus(StatusAlerta.RESOLVIDO); a.setResolvidoEm(LocalDateTime.now()); });
        alertaRepository.saveAll(alertas);
    }
}
