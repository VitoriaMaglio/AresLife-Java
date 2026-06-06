package com.fiap.areslife.service;

import com.fiap.areslife.entity.LogSistema;
import com.fiap.areslife.repository.LogSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogSistemaService {

    private final LogSistemaRepository logRepository;


    public void registrar(String operacao, String tabelaAfetada, String descricao) {
        LogSistema log = LogSistema.builder()
                .operacao(operacao)
                .tabelaAfetada(tabelaAfetada)
                .descricao(descricao)
                .usuario("areslife-api")
                .dataOperacao(LocalDateTime.now())
                .build();
        logRepository.save(log);
    }

    public List<LogSistema> listarTodos() {
        return logRepository.findAll();
    }

    public List<LogSistema> buscarPorTabela(String tabela) {
        return logRepository.findByTabelaAfetada(tabela);
    }

    public List<LogSistema> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return logRepository.findByDataOperacaoBetween(inicio, fim);
    }
}
