package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.TreinamentoRequest;
import com.fiap.areslife.dto.response.TreinamentoResponse;
import com.fiap.areslife.enums.StatusTreinamento;
import com.fiap.areslife.service.TreinamentoService;
import com.fiap.areslife.service.mapper.TreinamentoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/treinamentos")
@RequiredArgsConstructor
@Tag(name = "Treinamentos", description = "Gerenciamento de treinamentos dos habitantes")
public class TreinamentoController {

    private final TreinamentoService treinamentoService;

    @GetMapping
    @Operation(summary = "Listar treinamentos (filtros: habitanteId, status)")
    public ResponseEntity<List<TreinamentoResponse>> listar(
            @RequestParam(required = false) Long habitanteId,
            @RequestParam(required = false) StatusTreinamento status) {

        List<TreinamentoResponse> list = treinamentoService.listar(habitanteId, status)
                .stream()
                .map(TreinamentoMapper::toResponse)
                .toList();

        list.forEach(t -> t.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(t.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar treinamento por ID")
    public ResponseEntity<TreinamentoResponse> buscarPorId(@PathVariable Long id) {
        TreinamentoResponse response = TreinamentoMapper.toResponse(treinamentoService.buscarPorId(id));
        response.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TreinamentoController.class).listar(null, null)).withRel("treinamentos"),
            linkTo(methodOn(TreinamentoController.class).iniciar(id)).withRel("iniciar"),
            linkTo(methodOn(TreinamentoController.class).concluir(id, null)).withRel("concluir")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar novo treinamento")
    public ResponseEntity<TreinamentoResponse> criar(@Valid @RequestBody TreinamentoRequest request) {
        TreinamentoResponse response = TreinamentoMapper.toResponse(treinamentoService.criar(request));
        response.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(TreinamentoController.class).listar(null, null)).withRel("treinamentos")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar treinamento")
    public ResponseEntity<TreinamentoResponse> iniciar(@PathVariable Long id) {
        TreinamentoResponse response = TreinamentoMapper.toResponse(treinamentoService.iniciar(id));
        response.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(id)).withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir treinamento com nota final")
    public ResponseEntity<TreinamentoResponse> concluir(
            @PathVariable Long id,
            @RequestParam BigDecimal notaFinal) {
        TreinamentoResponse response = TreinamentoMapper.toResponse(treinamentoService.concluir(id, notaFinal));
        response.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TreinamentoController.class).listar(null, null)).withRel("treinamentos")
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendentes/colonia/{coloniaId}")
    @Operation(summary = "Listar treinamentos pendentes de uma colônia")
    public ResponseEntity<List<TreinamentoResponse>> pendentesPorColonia(@PathVariable Long coloniaId) {
        List<TreinamentoResponse> list = treinamentoService.listarPendentesPorColonia(coloniaId)
                .stream()
                .map(TreinamentoMapper::toResponse)
                .toList();
        list.forEach(t -> t.add(
            linkTo(methodOn(TreinamentoController.class).buscarPorId(t.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover treinamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        treinamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
