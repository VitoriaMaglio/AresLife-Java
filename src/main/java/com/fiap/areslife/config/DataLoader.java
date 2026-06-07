package com.fiap.areslife.config;

import com.fiap.areslife.entity.TuristaEspacial;
import com.fiap.areslife.entity.Usuario;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import com.fiap.areslife.repository.TuristaEspacialRepository;
import com.fiap.areslife.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    @Bean
    public CommandLineRunner carregarDados(
            TuristaEspacialRepository turistaRepo,
            UsuarioRepository usuarioRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ── Seed admin user se não existir
            if (usuarioRepo.findByEmail("admin@areslife.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@areslife.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                usuarioRepo.save(admin);
                System.out.println(" Admin criado: admin@areslife.com / admin123");
            }

            // ── Seed turistas iniciais
            if (turistaRepo.count() == 0) {
                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Marina Magalhães")
                        .idade(19)
                        .pais("Brasil")
                        .destino(Localizacao.MARTE)
                        .status(StatusTurista.AGUARDANDO)
                        .dataCadastro(LocalDate.now())
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Carlos Andrade")
                        .idade(34)
                        .pais("Argentina")
                        .destino(Localizacao.LUA)
                        .status(StatusTurista.AGUARDANDO)
                        .dataCadastro(LocalDate.now())
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Yuki Tanaka")
                        .idade(28)
                        .pais("Japão")
                        .destino(Localizacao.MARTE)
                        .status(StatusTurista.EMBARCADO)
                        .dataCadastro(LocalDate.now())
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Sarah Connor")
                        .idade(45)
                        .pais("Estados Unidos")
                        .destino(Localizacao.LUA)
                        .status(StatusTurista.EMBARCADO)
                        .dataCadastro(LocalDate.now())
                        .build());

                turistaRepo.save(TuristaEspacial.builder()
                        .nome("Ahmed Al-Rashid")
                        .idade(52)
                        .pais("Emirados Árabes")
                        .destino(Localizacao.MARTE)
                        .status(StatusTurista.EM_TRANSITO)
                        .dataCadastro(LocalDate.now())
                        .build());

                System.out.println(" Dados iniciais de turistas carregados!");
            }
        };
    }
}
