package com.fiap.areslife.config;

import com.fiap.areslife.entity.TuristaEspacial;
import com.fiap.areslife.repository.TuristaEspacialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    @Bean
    public CommandLineRunner carregarDados(TuristaEspacialRepository turistaRepo) {
        return args -> {
            if (turistaRepo.count() == 0) {
                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Marina Magalhães")
                        .idade(19)
                        .pais("Brasil")
                        .destino("Marte")
                        .status("Em treinamento")
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Carlos Andrade")
                        .idade(34)
                        .pais("Argentina")
                        .destino("Lua")
                        .status("Aguardando embarque")
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Yuki Tanaka")
                        .idade(28)
                        .pais("Japão")
                        .destino("Marte")
                        .status("Em treinamento")
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Sarah Connor")
                        .idade(45)
                        .pais("Estados Unidos")
                        .destino("Lua")
                        .status("Pronto para embarque")
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Ahmed Al-Rashid")
                        .idade(52)
                        .pais("Emirados Árabes")
                        .destino("Marte")
                        .status("Em trânsito")
                        .build());

                System.out.println("✅ Dados iniciais de Turistas Espaciais carregados com sucesso!");
            }
        };
    }
}
