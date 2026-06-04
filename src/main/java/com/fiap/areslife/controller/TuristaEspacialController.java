package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.TuristaEspacialRequest;
import com.fiap.areslife.dto.response.TuristaEspacialResponse;
import com.fiap.areslife.enums.Localizacao;
import com.fiap.areslife.enums.StatusTurista;
import com.fiap.areslife.service.TuristaEspacialService;
import com.fiap.areslife.service.mapper.TuristaEspacialMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/turistas")
@RequiredArgsConstructor
@Tag(name = "Turistas Espaciais", description = "Gerenciamento de turistas espaciais")
public class TuristaEspacialController {

    private final TuristaEspacialService passageiroService;

    @GetMapping
    @Operation(summary = "Listar turistas espaciais (filtros: destino, status)")
    public ResponseEntity<List<TuristaEspacialResponse>> listar(
            @RequestParam(required = false) Localizacao destino,
            @RequestParam(required = false) StatusTurista status) {

        List<TuristaEspacialResponse> list = passageiroService.listar(destino, status)
                .stream()
                .map(TuristaEspacialMapper::toResponse)
                .toList();

        list.forEach(t -> t.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(t.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar turista por ID")
    public ResponseEntity<TuristaEspacialResponse> buscarPorId(@PathVariable Long id) {
        TuristaEspacialResponse response = TuristaEspacialMapper.toResponse(passageiroService.buscarPorId(id));
        response.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TuristaEspacialController.class).listar(null, null)).withRel("turistas")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Registrar novo turista espacial")
    public ResponseEntity<TuristaEspacialResponse> registrar(@Valid @RequestBody TuristaEspacialRequest request) {
        TuristaEspacialResponse response = TuristaEspacialMapper.toResponse(passageiroService.registrar(request));
        response.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(TuristaEspacialController.class).listar(null, null)).withRel("turistas")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do turista")
    public ResponseEntity<TuristaEspacialResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusTurista novoStatus) {
        TuristaEspacialResponse response = TuristaEspacialMapper.toResponse(passageiroService.atualizarStatus(id, novoStatus));
        response.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(id)).withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/vincular")
    @Operation(summary = "Vincular turista a um habitante da colônia")
    public ResponseEntity<TuristaEspacialResponse> vincular(
            @PathVariable Long id,
            @RequestParam Long habitanteId) {
        TuristaEspacialResponse response = TuristaEspacialMapper.toResponse(passageiroService.vincularHabitante(id, habitanteId));
        response.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(id)).withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar turista espacial")
    public ResponseEntity<TuristaEspacialResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TuristaEspacialRequest request) {
        TuristaEspacialResponse response = TuristaEspacialMapper.toResponse(passageiroService.atualizar(id, request));
        response.add(
            linkTo(methodOn(TuristaEspacialController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TuristaEspacialController.class).listar(null, null)).withRel("turistas")
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover turista espacial")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        passageiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
