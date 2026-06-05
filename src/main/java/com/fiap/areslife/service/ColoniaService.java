package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.ColoniaRequest;
import com.fiap.areslife.dto.response.ColoniaResponse;
import com.fiap.areslife.entity.Alerta;
import com.fiap.areslife.entity.Colonia;
import com.fiap.areslife.enums.*;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.AlertaRepository;
import com.fiap.areslife.repository.ColoniaRepository;
import com.fiap.areslife.repository.HabitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColoniaService {

    private final ColoniaRepository coloniaRepository;
    private final HabitanteRepository habitanteRepository;
    private final AlertaRepository alertaRepository;
    private final LogSistemaService logService;

    public List<ColoniaResponse> listarTodas() {
        return coloniaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ColoniaResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public ColoniaResponse criar(ColoniaRequest request) {
        Colonia colonia = Colonia.builder()
                .nome(request.nome())
                .localizacao(request.localizacao())
                .capacidadeMax(request.capacidadeMax())
                .status(StatusColonia.ATIVA)
                .dataFundacao(request.dataFundacao())
                .descricao(request.descricao())
                .build();
        Colonia salva = coloniaRepository.save(colonia);
        logService.registrar("INSERT", "colonias",
                "Colônia criada: " + salva.getNome() + " (id=" + salva.getId() + "), local=" + salva.getLocalizacao());
        return toResponse(salva);
    }

    @Transactional
    public ColoniaResponse atualizar(Long id, ColoniaRequest request) {
        Colonia colonia = findOrThrow(id);
        colonia.setNome(request.nome());
        colonia.setLocalizacao(request.localizacao());
        colonia.setCapacidadeMax(request.capacidadeMax());
        colonia.setDataFundacao(request.dataFundacao());
        colonia.setDescricao(request.descricao());
        Colonia salva = coloniaRepository.save(colonia);
        logService.registrar("UPDATE", "colonias",
                "Colônia atualizada: " + salva.getNome() + " (id=" + id + ")");
        if (salva.getStatus() == StatusColonia.EMERGENCIA) {
            criarAlertaEmergencia(salva);
        }
        return toResponse(salva);
    }

    @Transactional
    public void deletar(Long id) {
        Colonia colonia = findOrThrow(id);
        long habitantesAtivos = habitanteRepository.countByColoniaIdAndStatus(id, StatusHabitante.ATIVO);
        if (habitantesAtivos > 0) {
            throw new BusinessException(
                    "Não é possível deletar a colônia pois existem " + habitantesAtivos + " habitante(s) ativo(s).");
        }
        String nome = colonia.getNome();
        coloniaRepository.delete(colonia);
        logService.registrar("DELETE", "colonias", "Colônia removida: " + nome + " (id=" + id + ")");
    }

    public Colonia findOrThrow(Long id) {
        return coloniaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colônia não encontrada com id: " + id));
    }

    private void criarAlertaEmergencia(Colonia colonia) {
        Alerta alerta = Alerta.builder()
                .colonia(colonia)
                .tipoAlerta(TipoAlerta.EMERGENCIA)
                .descricao("Colônia " + colonia.getNome() + " entrou em estado de EMERGÊNCIA.")
                .severidade(SeveridadeAlerta.CRITICA)
                .dataEmissao(LocalDateTime.now())
                .status(StatusAlerta.ABERTO)
                .build();
        alertaRepository.save(alerta);
        logService.registrar("INSERT", "alertas",
                "Alerta de emergência gerado para colônia id=" + colonia.getId());
    }

    private ColoniaResponse toResponse(Colonia colonia) {
        long total = habitanteRepository.countByColoniaIdAndStatus(colonia.getId(), StatusHabitante.ATIVO);
        return new ColoniaResponse(
                colonia.getId(), colonia.getNome(), colonia.getLocalizacao(),
                colonia.getCapacidadeMax(), colonia.getStatus(), colonia.getDataFundacao(),
                colonia.getDescricao(), total);
    }
}
