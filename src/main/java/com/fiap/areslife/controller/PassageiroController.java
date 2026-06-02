package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.PassageiroRequest;
import com.fiap.areslife.entity.Passageiro;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusPassageiro;
import com.fiap.areslife.service.PassageiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passageiros")
@RequiredArgsConstructor
@Tag(name = "Passageiros", description = "Gerenciamento de passageiros espaciais")
public class PassageiroController {

    private final PassageiroService passageiroService;

    @GetMapping
    @Operation(summary = "Listar passageiros (filtros: destino, status)")
    public ResponseEntity<List<Passageiro>> listar(
            @RequestParam(required = false) Localizacao destino,
            @RequestParam(required = false) StatusPassageiro status) {
        return ResponseEntity.ok(passageiroService.listar(destino, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar passageiro por ID")
    public ResponseEntity<Passageiro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(passageiroService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar novo passageiro")
    public ResponseEntity<Passageiro> registrar(@Valid @RequestBody PassageiroRequest request) {
        return ResponseEntity.status(201).body(passageiroService.registrar(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do passageiro")
    public ResponseEntity<Passageiro> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPassageiro novoStatus) {
        return ResponseEntity.ok(passageiroService.atualizarStatus(id, novoStatus));
    }

    @PatchMapping("/{id}/vincular")
    @Operation(summary = "Vincular passageiro a um habitante da colônia")
    public ResponseEntity<Passageiro> vincular(
            @PathVariable Long id,
            @RequestParam Long habitanteId) {
        return ResponseEntity.ok(passageiroService.vincularHabitante(id, habitanteId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover passageiro")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        passageiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
