package com.fiap.areslife.service;

import com.fiap.areslife.dto.request.PassageiroRequest;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TuristaEspacialService {

    private final TuristaEspacialRepository passageiroRepository;
    private final HabitanteRepository habitanteRepository;

    public List<TuristaEspacial> listar(Localizacao destino, StatusTurista status) {
        if (destino != null && status != null) {
            return passageiroRepository.findByDestinoAndStatus(destino, status);
        } else if (destino != null) {
            return passageiroRepository.findByDestino(destino);
        } else if (status != null) {
            return passageiroRepository.findByStatus(status);
        }
        return passageiroRepository.findAll();
    }

    public TuristaEspacial buscarPorId(Long id) {
        return passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado com id: " + id));
    }

    @Transactional
    public TuristaEspacial registrar(PassageiroRequest request) {
        if (request.idade() < 18 || request.idade() > 99) {
            throw new BusinessException("Idade do passageiro deve ser entre 18 e 99 anos.");
        }

        TuristaEspacial turistaEspacial = TuristaEspacial.builder()
                .nome(request.nome())
                .idade(request.idade())
                .pais(request.pais())
                .destino(request.destino())
                .status(StatusTurista.AGUARDANDO.AGUARDANDO)
                .dataCadastro(LocalDate.now())
                .build();

        return passageiroRepository.save(turistaEspacial);
    }

    @Transactional
    public TuristaEspacial atualizarStatus(Long id, StatusTurista novoStatus) {
        TuristaEspacial turistaEspacial = buscarPorId(id);
        validarTransicaoStatus(turistaEspacial.getStatus(), novoStatus);
        turistaEspacial.setStatus(novoStatus);
        return passageiroRepository.save(turistaEspacial);
    }

    @Transactional
    public TuristaEspacial vincularHabitante(Long id, Long habitanteId) {
        TuristaEspacial passageiro = buscarPorId(id);
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

    private void validarTransicaoStatus(StatusTurista atual, StatusTurista novo) {
        boolean valido = switch (atual) {
            case AGUARDANDO -> novo == StatusTurista.EMBARCADO || novo == StatusTurista.CANCELADO;
            case EMBARCADO -> novo == StatusTurista.EM_TRANSITO || novo == StatusTurista.CANCELADO;
            case EM_TRANSITO -> novo == StatusTurista.CHEGOU || novo == StatusTurista.CANCELADO;
            case CHEGOU, CANCELADO -> false;
        };

        if (!valido) {
            throw new BusinessException("Transição de status inválida: " + atual + " → " + novo);
        }
    }
}
