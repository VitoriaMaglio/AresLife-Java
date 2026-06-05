package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.TuristaEspacialRequest;
import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.entity.TuristaEspacial;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.TuristaEspacialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TuristaEspacialService {

    private final TuristaEspacialRepository passageiroRepository;
    private final HabitanteRepository habitanteRepository;
    private final LogSistemaService logService;

    public List<TuristaEspacial> listar(Localizacao destino, StatusTurista status) {
        if (destino != null && status != null)
            return passageiroRepository.findByDestinoAndStatus(destino, status);
        if (destino != null)
            return passageiroRepository.findByDestino(destino);
        if (status != null)
            return passageiroRepository.findByStatus(status);
        return passageiroRepository.findAll();
    }

    public TuristaEspacial buscarPorId(Long id) {
        return passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turista não encontrado com id: " + id));
    }

    @Transactional
    public TuristaEspacial registrar(TuristaEspacialRequest request) {
        TuristaEspacial turista = TuristaEspacial.builder()
                .nome(request.nome())
                .idade(request.idade())
                .pais(request.pais())
                .destino(request.destino())
                .status(StatusTurista.AGUARDANDO)
                .dataCadastro(LocalDate.now())
                .build();

        TuristaEspacial salvo = passageiroRepository.save(turista);
        logService.registrar("INSERT", "turistas_espaciais",
                "Turista registrado: " + salvo.getNome() + " (id=" + salvo.getId() + "), destino=" + salvo.getDestino());
        return salvo;
    }

    @Transactional
    public TuristaEspacial atualizar(Long id, TuristaEspacialRequest request) {
        TuristaEspacial turista = buscarPorId(id);

        turista.setNome(request.nome());
        turista.setIdade(request.idade());
        turista.setPais(request.pais());
        turista.setDestino(request.destino());
        if (request.status() != null) turista.setStatus(request.status());

        TuristaEspacial salvo = passageiroRepository.save(turista);
        logService.registrar("UPDATE", "turistas_espaciais",
                "Turista atualizado: " + salvo.getNome() + " (id=" + id + ")");
        return salvo;
    }

    @Transactional
    public TuristaEspacial atualizarStatus(Long id, StatusTurista novoStatus) {
        TuristaEspacial turista = buscarPorId(id);
        StatusTurista statusAnterior = turista.getStatus();
        validarTransicaoStatus(statusAnterior, novoStatus);
        turista.setStatus(novoStatus);
        TuristaEspacial salvo = passageiroRepository.save(turista);
        logService.registrar("UPDATE", "turistas_espaciais",
                "Status do turista id=" + id + " alterado: " + statusAnterior + " → " + novoStatus);
        return salvo;
    }

    @Transactional
    public TuristaEspacial vincularHabitante(Long id, Long habitanteId) {
        TuristaEspacial turista = buscarPorId(id);
        Habitante habitante = habitanteRepository.findById(habitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitante não encontrado com id: " + habitanteId));
        turista.setHabitante(habitante);
        TuristaEspacial salvo = passageiroRepository.save(turista);
        logService.registrar("UPDATE", "turistas_espaciais",
                "Turista id=" + id + " vinculado ao habitante id=" + habitanteId);
        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        TuristaEspacial turista = buscarPorId(id);
        String nome = turista.getNome();
        passageiroRepository.deleteById(id);
        logService.registrar("DELETE", "turistas_espaciais",
                "Turista removido: " + nome + " (id=" + id + ")");
    }

    private void validarTransicaoStatus(StatusTurista atual, StatusTurista novo) {
        boolean valido = switch (atual) {
            case AGUARDANDO  -> novo == StatusTurista.EMBARCADO  || novo == StatusTurista.CANCELADO;
            case EMBARCADO   -> novo == StatusTurista.EM_TRANSITO|| novo == StatusTurista.CANCELADO;
            case EM_TRANSITO -> novo == StatusTurista.CHEGOU     || novo == StatusTurista.CANCELADO;
            case CHEGOU, CANCELADO -> false;
        };
        if (!valido)
            throw new BusinessException("Transição de status inválida: " + atual + " → " + novo);
    }
}
