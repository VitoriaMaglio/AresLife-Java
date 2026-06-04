package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.LoginRequest;
import com.fiap.areslife.entity.Usuario;
import com.fiap.areslife.repository.UsuarioRepository;
import com.fiap.areslife.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtService jwtService;


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // O manager já busca o usuário, compara o hash BCrypt e lança BadCredentialsException se errar
        manager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        return jwtService.gerarToken(request.email());
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginRequest request) {

        Usuario usuario = new Usuario();

        usuario.setEmail(request.email());
        usuario.setNome("Admin");

        // 🔥 ESSENCIAL: SEM ISSO LOGIN NUNCA VAI FUNCIONAR
        usuario.setSenha(passwordEncoder.encode(request.senha()));

        usuario.setRole("ADMIN");

        usuarioRepository.save(usuario);

        return "Usuário criado com sucesso";
    }
}
