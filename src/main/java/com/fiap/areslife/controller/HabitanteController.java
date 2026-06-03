package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.HabitanteRequest;
import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.service.HabitanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitantes")
@RequiredArgsConstructor
@Tag(name = "Habitantes", description = "Gerenciamento de habitantes das colônias (astronautas e turistas)")
public class HabitanteController {

    private final HabitanteService habitanteService;


    @GetMapping
    public ResponseEntity<List<Habitante>> listar(
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) String tipo) {

        return ResponseEntity.ok(
                habitanteService.listar(coloniaId, tipo)
        );
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar habitante por ID")
    public ResponseEntity<Habitante> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(habitanteService.buscarPorId(id));
    }

    @PostMapping("/astronautas")
    @Operation(summary = "Registrar novo astronauta")
    public ResponseEntity<Habitante> criarAstronauta(@Valid @RequestBody HabitanteRequest request) {
        return ResponseEntity.status(201).body(habitanteService.criarAstronauta(request));
    }

    @PostMapping("/turistas")
    @Operation(summary = "Registrar novo turista (cria treinamentos obrigatórios automaticamente)")
    public ResponseEntity<Habitante> criarTurista(@Valid @RequestBody HabitanteRequest request) {
        return ResponseEntity.status(201).body(habitanteService.criarTurista(request));
    }

    @PatchMapping("/{id}/saida")
    @Operation(summary = "Registrar saída do habitante da colônia")
    public ResponseEntity<Habitante> registrarSaida(@PathVariable Long id) {
        return ResponseEntity.ok(habitanteService.registrarSaida(id));
    }

    @PatchMapping("/{id}/transferir")
    @Operation(summary = "Transferir habitante para outra colônia")
    public ResponseEntity<Habitante> transferir(
            @PathVariable Long id,
            @RequestParam Long coloniaDestinoId) {
        return ResponseEntity.ok(habitanteService.transferir(id, coloniaDestinoId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover habitante")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        habitanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
