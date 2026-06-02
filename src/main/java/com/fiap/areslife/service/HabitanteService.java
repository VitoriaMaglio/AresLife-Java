package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.HabitanteRequest;
import com.fiap.areslife.entity.*;
import com.fiap.areslife.enums.StatusHabitante;
import com.fiap.areslife.enums.StatusSaude;
import com.fiap.areslife.enums.StatusTreinamento;
import com.fiap.areslife.enums.TipoHabitante;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.SaudeHabitanteRepository;
import com.fiap.areslife.repository.TreinamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Habitante> listar(Long coloniaId, TipoHabitante tipo) {
        if (coloniaId != null && tipo != null) {
            return habitanteRepository.findByColoniaIdAndTipo(coloniaId, tipo);
        } else if (coloniaId != null) {
            return habitanteRepository.findByColoniaId(coloniaId);
        }
        return habitanteRepository.findAll();
    }

    public Habitante buscarPorId(Long id) {
        return habitanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitante não encontrado com id: " + id));
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
        return salvo;
    }

    @Transactional
    public Habitante registrarSaida(Long id) {
        Habitante habitante = buscarPorId(id);
        habitante.setDataSaida(LocalDate.now());
        habitante.setStatus(StatusHabitante.INATIVO);
        return habitanteRepository.save(habitante);
    }

    @Transactional
    public Habitante transferir(Long id, Long coloniaDestinoId) {
        Habitante habitante = buscarPorId(id);
        Colonia destino = validarCapacidade(coloniaDestinoId);
        habitante.setColonia(destino);
        return habitanteRepository.save(habitante);
    }

    @Transactional
    public void deletar(Long id) {
        if (!habitanteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Habitante não encontrado com id: " + id);
        }
        habitanteRepository.deleteById(id);
    }

    // ---- helpers ----

    private Colonia validarCapacidade(Long coloniaId) {
        Colonia colonia = coloniaService.findOrThrow(coloniaId);
        long ativos = habitanteRepository.countByColoniaIdAndStatus(coloniaId, StatusHabitante.ATIVO);
        if (ativos >= colonia.getCapacidadeMax()) {
            throw new BusinessException("Colônia atingiu a capacidade máxima de " + colonia.getCapacidadeMax() + " habitantes.");
        }
        return colonia;
    }

    private void preencherDadosBase(Habitante habitante, HabitanteRequest req, Colonia colonia) {
        habitante.setColonia(colonia);
        habitante.setNome(req.nome());
        habitante.setNacionalidade(req.nacionalidade());
        habitante.setDataChegada(req.dataChegada());
        habitante.setStatus(StatusHabitante.ATIVO);
    }

    private void criarSaudeInicial(Habitante habitante) {
        SinaisVitais sinais = SinaisVitais.builder()
                .pressaoArterial("120/80")
                .frequenciaCardiaca(72)
                .saturacaoOxigenio(new java.math.BigDecimal("98.0"))
                .temperaturaCorporal(new java.math.BigDecimal("36.5"))
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
        Treinamento t1 = Treinamento.builder()
                .habitante(habitante)
                .tipoTreinamento("Orientação Espacial Básica")
                .cargaHoraria(8)
                .status(StatusTreinamento.EM_ANDAMENTO)
                .dataInicio(LocalDate.now())
                .build();

        Treinamento t2 = Treinamento.builder()
                .habitante(habitante)
                .tipoTreinamento("Procedimentos de Emergência")
                .cargaHoraria(16)
                .status(StatusTreinamento.PENDENTE)
                .build();

        treinamentoRepository.save(t1);
        treinamentoRepository.save(t2);
    }
}
