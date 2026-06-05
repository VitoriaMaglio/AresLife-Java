package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.LoginRequest;
import com.fiap.areslife.entity.Usuario;
import com.fiap.areslife.repository.UsuarioRepository;
import com.fiap.areslife.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e registro de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Autenticar e obter token JWT")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        String token = jwtService.gerarToken(request.email());
        return ResponseEntity.ok(Map.of("token", token, "tipo", "Bearer"));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário (Admin)")
    public ResponseEntity<Map<String, String>> register(@RequestBody LoginRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest()
                .body(Map.of("erro", "Email já cadastrado."));
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setNome("Usuário");
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole("USER");
        usuarioRepository.save(usuario);
        return ResponseEntity.status(201)
            .body(Map.of("mensagem", "Usuário criado com sucesso"));
    }
}
