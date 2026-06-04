package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.HabitanteRequest;
import com.fiap.areslife.dto.response.HabitanteResponse;
import com.fiap.areslife.service.HabitanteService;
import com.fiap.areslife.service.mapper.HabitanteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/habitantes")
@RequiredArgsConstructor
@Tag(name = "Habitantes", description = "Gerenciamento de habitantes das colônias (astronautas e turistas)")
public class HabitanteController {

    private final HabitanteService habitanteService;

    @GetMapping
    @Operation(summary = "Listar habitantes (filtros: coloniaId, tipo)")
    public ResponseEntity<List<HabitanteResponse>> listar(
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) String tipo) {

        List<HabitanteResponse> list = habitanteService.listar(coloniaId, tipo)
                .stream()
                .map(HabitanteMapper::toResponse)
                .toList();

        list.forEach(h -> h.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(h.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar habitante por ID")
    public ResponseEntity<HabitanteResponse> buscarPorId(@PathVariable Long id) {
        HabitanteResponse response = HabitanteMapper.toResponse(habitanteService.buscarPorId(id));
        response.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(HabitanteController.class).listar(null, null)).withRel("habitantes"),
            linkTo(methodOn(HabitanteController.class).registrarSaida(id)).withRel("registrar-saida")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/astronautas")
    @Operation(summary = "Criar astronauta")
    public ResponseEntity<HabitanteResponse> criarAstronauta(@Valid @RequestBody HabitanteRequest request) {
        HabitanteResponse response = HabitanteMapper.toResponse(habitanteService.criarAstronauta(request));
        response.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(HabitanteController.class).listar(null, null)).withRel("habitantes")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/turistas")
    @Operation(summary = "Criar turista habitante")
    public ResponseEntity<HabitanteResponse> criarTurista(@Valid @RequestBody HabitanteRequest request) {
        HabitanteResponse response = HabitanteMapper.toResponse(habitanteService.criarTurista(request));
        response.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(HabitanteController.class).listar(null, null)).withRel("habitantes")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/saida")
    @Operation(summary = "Registrar saída do habitante da colônia")
    public ResponseEntity<HabitanteResponse> registrarSaida(@PathVariable Long id) {
        HabitanteResponse response = HabitanteMapper.toResponse(habitanteService.registrarSaida(id));
        response.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(id)).withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/transferir")
    @Operation(summary = "Transferir habitante para outra colônia")
    public ResponseEntity<HabitanteResponse> transferir(
            @PathVariable Long id,
            @RequestParam Long coloniaDestinoId) {
        HabitanteResponse response = HabitanteMapper.toResponse(habitanteService.transferir(id, coloniaDestinoId));
        response.add(
            linkTo(methodOn(HabitanteController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(HabitanteController.class).listar(null, null)).withRel("habitantes")
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover habitante")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        habitanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
