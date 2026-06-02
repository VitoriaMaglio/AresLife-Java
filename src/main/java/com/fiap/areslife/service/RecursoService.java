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
        Colonia colonia = coloniaService.findOrThrow(request.coloniaId());

        if (recursoRepository.existsByColoniaIdAndTipoRecurso(request.coloniaId(), request.tipoRecurso())) {
            throw new BusinessException("Já existe um recurso do tipo " + request.tipoRecurso() + " nesta colônia.");
        }

        if (request.nivelCritico().compareTo(request.nivelMaximo()) >= 0) {
            throw new BusinessException("Nível crítico deve ser menor que o nível máximo.");
        }

        Recurso recurso = Recurso.builder()
                .colonia(colonia)
                .tipoRecurso(request.tipoRecurso())
                .quantidade(request.quantidade())
                .unidade(request.unidade())
                .nivelCritico(request.nivelCritico())
                .nivelMaximo(request.nivelMaximo())
                .dataAtualizacao(LocalDate.now())
                .build();

        return recursoRepository.save(recurso);
    }

    @Transactional
    public Recurso abastecer(Long id, AbastecerRequest request) {
        Recurso recurso = buscarPorId(id);
        BigDecimal novaQtd = recurso.getQuantidade().add(request.quantidade());

        if (novaQtd.compareTo(recurso.getNivelMaximo()) > 0) {
            novaQtd = recurso.getNivelMaximo();
        }

        recurso.setQuantidade(novaQtd);
        recurso.setDataAtualizacao(LocalDate.now());

        // Resolver alertas de recurso crítico se nível voltou ao normal
        if (novaQtd.compareTo(recurso.getNivelCritico()) > 0) {
            resolverAlertasRecurso(recurso.getColonia().getId());
        }

        registrarMonitoramento(recurso, novaQtd, "NORMAL", "Abastecimento realizado.");
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void simularDia(Long coloniaId) {
        Colonia colonia = coloniaService.findOrThrow(coloniaId);
        long numHabitantes = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);

        if (numHabitantes == 0) return;

        List<Recurso> recursos = recursoRepository.findByColoniaId(coloniaId);

        for (Recurso recurso : recursos) {
            BigDecimal consumo = calcularConsumoDiario(recurso.getTipoRecurso())
                    .multiply(BigDecimal.valueOf(numHabitantes));

            BigDecimal novaQtd = recurso.getQuantidade().subtract(consumo);
            if (novaQtd.compareTo(BigDecimal.ZERO) < 0) novaQtd = BigDecimal.ZERO;

            recurso.setQuantidade(novaQtd);
            recurso.setDataAtualizacao(LocalDate.now());

            String statusMon = "NORMAL";
            if (novaQtd.compareTo(recurso.getNivelCritico()) <= 0) {
                statusMon = "CRITICO";
                criarAlertaRecursoCritico(colonia, recurso);
            }

            registrarMonitoramento(recurso, novaQtd, statusMon, "Consumo diário simulado.");
            recursoRepository.save(recurso);
        }
    }

    public int calcularAutonomia(Long coloniaId, TipoRecurso tipo) {
        Recurso recurso = recursoRepository.findByColoniaIdAndTipoRecurso(coloniaId, tipo)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso do tipo " + tipo + " não encontrado na colônia."));

        long numHabitantes = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);
        if (numHabitantes == 0) return Integer.MAX_VALUE;

        BigDecimal consumoDiario = calcularConsumoDiario(tipo).multiply(BigDecimal.valueOf(numHabitantes));
        if (consumoDiario.compareTo(BigDecimal.ZERO) == 0) return Integer.MAX_VALUE;

        return recurso.getQuantidade().divide(consumoDiario, 0, RoundingMode.FLOOR).intValue();
    }

    @Transactional
    public void deletar(Long id) {
        if (!recursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado com id: " + id);
        }
        recursoRepository.deleteById(id);
    }

    // ---- helpers ----

    private BigDecimal calcularConsumoDiario(TipoRecurso tipo) {
        return switch (tipo) {
            case OXIGENIO -> new BigDecimal("0.84");
            case AGUA -> new BigDecimal("3.00");
            case ENERGIA -> new BigDecimal("15.00");
            case ALIMENTACAO -> new BigDecimal("0.60");
        };
    }

    private void registrarMonitoramento(Recurso recurso, BigDecimal valor, String status, String obs) {

        MonitoramentoRecurso mon = MonitoramentoRecurso.builder()
                .recurso(recurso)
                .colonia(recurso.getColonia())
                .valorRegistrado(valor)
                .dataRegistro(LocalDateTime.now())
                .status(status)
                .observacao(obs)
                .build();

        monitoramentoRepository.save(mon);
    }

    private void criarAlertaRecursoCritico(Colonia colonia, Recurso recurso) {
        Alerta alerta = Alerta.builder()
                .colonia(colonia)
                .tipoAlerta(TipoAlerta.RECURSO_CRITICO)
                .descricao("Recurso " + recurso.getTipoRecurso() + " atingiu nível crítico: " + recurso.getQuantidade() + " " + recurso.getUnidade())
                .severidade(SeveridadeAlerta.CRITICA)
                .dataEmissao(LocalDateTime.now())
                .status(StatusAlerta.ABERTO)
                .build();
        alertaRepository.save(alerta);
    }

    private void resolverAlertasRecurso(Long coloniaId) {
        List<Alerta> alertas = alertaRepository.findByColoniaIdAndTipoAlertaAndStatusNot(
                coloniaId, TipoAlerta.RECURSO_CRITICO, StatusAlerta.RESOLVIDO);
        alertas.forEach(a -> {
            a.setStatus(StatusAlerta.RESOLVIDO);
            a.setResolvidoEm(LocalDateTime.now());
        });
        alertaRepository.saveAll(alertas);
    }
}
