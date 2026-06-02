package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.PassageiroRequest;
import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.entity.Passageiro;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusPassageiro;
import com.fiap.areslife.exception.BusinessException;
import com.fiap.areslife.exception.ResourceNotFoundException;
import com.fiap.areslife.repository.HabitanteRepository;
import com.fiap.areslife.repository.PassageiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassageiroService {

    private final PassageiroRepository passageiroRepository;
    private final HabitanteRepository habitanteRepository;

    public List<Passageiro> listar(Localizacao destino, StatusPassageiro status) {
        if (destino != null && status != null) {
            return passageiroRepository.findByDestinoAndStatus(destino, status);
        } else if (destino != null) {
            return passageiroRepository.findByDestino(destino);
        } else if (status != null) {
            return passageiroRepository.findByStatus(status);
        }
        return passageiroRepository.findAll();
    }

    public Passageiro buscarPorId(Long id) {
        return passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado com id: " + id));
    }

    @Transactional
    public Passageiro registrar(PassageiroRequest request) {
        if (request.idade() < 18 || request.idade() > 99) {
            throw new BusinessException("Idade do passageiro deve ser entre 18 e 99 anos.");
        }

        Passageiro passageiro = Passageiro.builder()
                .nome(request.nome())
                .idade(request.idade())
                .pais(request.pais())
                .destino(request.destino())
                .status(StatusPassageiro.AGUARDANDO)
                .dataCadastro(LocalDateTime.now())
                .build();

        return passageiroRepository.save(passageiro);
    }

    @Transactional
    public Passageiro atualizarStatus(Long id, StatusPassageiro novoStatus) {
        Passageiro passageiro = buscarPorId(id);
        validarTransicaoStatus(passageiro.getStatus(), novoStatus);
        passageiro.setStatus(novoStatus);
        return passageiroRepository.save(passageiro);
    }

    @Transactional
    public Passageiro vincularHabitante(Long id, Long habitanteId) {
        Passageiro passageiro = buscarPorId(id);
        Habitante habitante = habitanteRepository.findById(habitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitante não encontrado com id: " + habitanteId));
        passageiro.setHabitante(habitante);
        return passageiroRepository.save(passageiro);
    }

    @Transactional
    public void deletar(Long id) {
        if (!passageiroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passageiro não encontrado com id: " + id);
        }
        passageiroRepository.deleteById(id);
    }

    // ---- helpers ----

    private void validarTransicaoStatus(StatusPassageiro atual, StatusPassageiro novo) {
        boolean valido = switch (atual) {
            case AGUARDANDO -> novo == StatusPassageiro.EMBARCADO || novo == StatusPassageiro.CANCELADO;
            case EMBARCADO -> novo == StatusPassageiro.EM_TRANSITO || novo == StatusPassageiro.CANCELADO;
            case EM_TRANSITO -> novo == StatusPassageiro.CHEGOU || novo == StatusPassageiro.CANCELADO;
            case CHEGOU, CANCELADO -> false;
        };

        if (!valido) {
            throw new BusinessException("Transição de status inválida: " + atual + " → " + novo);
        }
    }
}
