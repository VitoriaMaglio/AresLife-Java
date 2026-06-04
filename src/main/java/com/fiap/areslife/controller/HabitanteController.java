package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.HabitanteRequest;
import com.fiap.areslife.dto.response.HabitanteResponse;
import com.fiap.areslife.entity.Habitante;
import com.fiap.areslife.service.HabitanteService;
import com.fiap.areslife.service.mapper.HabitanteMapper;
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
    public ResponseEntity<List<HabitanteResponse>> listar(
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) String tipo) {

        return ResponseEntity.ok(
                habitanteService.listar(coloniaId, tipo)
                        .stream()
                        .map(HabitanteMapper::toResponse)
                        .toList()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<HabitanteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                HabitanteMapper.toResponse(habitanteService.buscarPorId(id))
        );
    }
    @PostMapping("/astronautas")
    public ResponseEntity<HabitanteResponse> criarAstronauta(@Valid @RequestBody HabitanteRequest request) {
        return ResponseEntity.status(201).body(
                HabitanteMapper.toResponse(habitanteService.criarAstronauta(request))
        );
    }
    @PostMapping("/turistas")
    public ResponseEntity<HabitanteResponse> criarTurista(@Valid @RequestBody HabitanteRequest request) {
        return ResponseEntity.status(201).body(
                HabitanteMapper.toResponse(habitanteService.criarTurista(request))
        );
    }
    @PatchMapping("/{id}/saida")
    @Operation(summary = "Registrar saída do habitante da colônia")
    public ResponseEntity<HabitanteResponse> registrarSaida(@PathVariable Long id) {
        return ResponseEntity.ok(
                HabitanteMapper.toResponse(
                        habitanteService.registrarSaida(id)
                )
        );
    }

    @PatchMapping("/{id}/transferir")
    public ResponseEntity<HabitanteResponse> transferir(
            @PathVariable Long id,
            @RequestParam Long coloniaDestinoId) {

        return ResponseEntity.ok(
                HabitanteMapper.toResponse(
                        habitanteService.transferir(id, coloniaDestinoId)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover habitante")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        habitanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    
}
