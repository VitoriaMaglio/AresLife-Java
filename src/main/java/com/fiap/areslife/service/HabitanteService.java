package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.HabitanteRequest;
import com.fiap.areslife.entity.*;
import com.fiap.areslife.enums.StatusHabitante;
import com.fiap.areslife.enums.StatusSaude;
import com.fiap.areslife.enums.StatusTreinamento;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.SaudeHabitanteRepository;
import com.fiap.areslife.repository.TreinamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitanteService {

    private final HabitanteRepository habitanteRepository;
    private final ColoniaService coloniaService;
    private final TreinamentoRepository treinamentoRepository;
    private final SaudeHabitanteRepository saudeRepository;
    private final LogSistemaService logService;

    public List<Habitante> listar(Long coloniaId, String tipo) {
        List<Habitante> habitantes = coloniaId != null
                ? habitanteRepository.findByColoniaId(coloniaId)
                : habitanteRepository.findAll();
        if (tipo == null) return habitantes;
        return habitantes.stream()
                .filter(h -> tipo.equalsIgnoreCase(h.getClass().getSimpleName()))
                .toList();
    }

    @Transactional
    public Habitante criarAstronauta(HabitanteRequest request) {
        Colonia colonia = validarCapacidade(request.coloniaId());
        Astronauta astronauta = new Astronauta();
        preencherDadosBase(astronauta, request, colonia);
        astronauta.setEspecialidade(request.especialidade());
        astronauta.setMissaoAtual(request.missaoAtual());
        Astronauta salvo = (Astronauta) habitanteRepository.save(astronauta);
        criarSaudeInicial(salvo);
        logService.registrar("INSERT", "habitantes",
                "Astronauta criado: " + salvo.getNome() + " (id=" + salvo.getId() + "), colônia id=" + request.coloniaId());
        return salvo;
    }

    @Transactional
    public Habitante criarTurista(HabitanteRequest request) {
        Colonia colonia = validarCapacidade(request.coloniaId());
        Turista turista = new Turista();
        preencherDadosBase(turista, request, colonia);
        turista.setAgenciaTurismo(request.agenciaTurismo());
        turista.setPacoteSelecionado(request.pacoteSelecionado());
        Turista salvo = (Turista) habitanteRepository.save(turista);
        criarSaudeInicial(salvo);
        criarTreinamentosObrigatorios(salvo);
        logService.registrar("INSERT", "habitantes",
                "Turista (habitante) criado: " + salvo.getNome() + " (id=" + salvo.getId() + "), colônia id=" + request.coloniaId());
        return salvo;
    }

    @Transactional(readOnly = true)
    public Habitante buscarPorId(Long id) {
        return habitanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitante não encontrado: " + id));
    }

    @Transactional
    public Habitante registrarSaida(Long id) {
        Habitante habitante = buscarPorId(id);
        habitante.setDataSaida(LocalDate.now());
        habitante.setStatus(StatusHabitante.INATIVO);
        Habitante salvo = habitanteRepository.save(habitante);
        logService.registrar("UPDATE", "habitantes",
                "Saída registrada para habitante: " + salvo.getNome() + " (id=" + id + ")");
        return salvo;
    }

    @Transactional
    public Habitante transferir(Long id, Long coloniaDestinoId) {
        Habitante habitante = buscarPorId(id);
        Colonia destino = validarCapacidade(coloniaDestinoId);
        habitante.setColonia(destino);
        Habitante salvo = habitanteRepository.save(habitante);
        logService.registrar("UPDATE", "habitantes",
                "Habitante id=" + id + " transferido para colônia id=" + coloniaDestinoId);
        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        Habitante habitante = buscarPorId(id);
        String nome = habitante.getNome();
        habitanteRepository.deleteById(id);
        logService.registrar("DELETE", "habitantes",
                "Habitante removido: " + nome + " (id=" + id + ")");
    }

    private Colonia validarCapacidade(Long coloniaId) {
        Colonia colonia = coloniaService.findOrThrow(coloniaId);
        long ativos = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);
        if (ativos >= colonia.getCapacidadeMax())
            throw new BusinessException("Colônia atingiu a capacidade máxima de " + colonia.getCapacidadeMax() + " habitantes.");
        return colonia;
    }

    private void preencherDadosBase(Habitante h, HabitanteRequest req, Colonia colonia) {
        h.setColonia(colonia);
        h.setNome(req.nome());
        h.setNacionalidade(req.nacionalidade());
        h.setDataChegada(req.dataChegada());
        h.setStatus(StatusHabitante.ATIVO);
    }

    private void criarSaudeInicial(Habitante habitante) {
        SinaisVitais sinais = SinaisVitais.builder()
                .pressaoArterial("120/80")
                .frequenciaCardiaca(72)
                .saturacaoOxigenio(new BigDecimal("98.0"))
                .temperaturaCorporal(new BigDecimal("36.5"))
                .build();
        SaudeHabitante saude = SaudeHabitante.builder()
                .habitante(habitante)
                .sinaisVitais(sinais)
                .statusSaude(StatusSaude.NORMAL)
                .dataRegistro(LocalDateTime.now())
                .observacoes("Registro inicial na chegada.")
                .build();
        saudeRepository.save(saude);
    }

    private void criarTreinamentosObrigatorios(Habitante habitante) {
        treinamentoRepository.save(Treinamento.builder()
                .habitante(habitante)
                .tipoTreinamento("Orientação Espacial Básica")
                .cargaHoraria(8)
                .status(StatusTreinamento.EM_ANDAMENTO)
                .dataInicio(LocalDate.now())
                .build());
        treinamentoRepository.save(Treinamento.builder()
                .habitante(habitante)
                .tipoTreinamento("Procedimentos de Emergência")
                .cargaHoraria(16)
                .status(StatusTreinamento.PENDENTE)
                .build());
    }
}
