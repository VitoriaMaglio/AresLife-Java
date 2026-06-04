package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.ColoniaRequest;
import com.fiap.areslife.dto.response.ColoniaResponse;
import com.fiap.areslife.service.ColoniaService;
import com.fiap.areslife.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/colonias")
@RequiredArgsConstructor
@Tag(name = "Colônias", description = "Gerenciamento de colônias espaciais")
public class ColoniaController {

    private final ColoniaService coloniaService;
    private final RecursoService recursoService;

    @GetMapping
    @Operation(summary = "Listar todas as colônias")
    public ResponseEntity<List<ColoniaResponse>> listarTodas() {
        List<ColoniaResponse> list = coloniaService.listarTodas();
        list.forEach(c -> c.add(
            linkTo(methodOn(ColoniaController.class).buscarPorId(c.getId())).withSelfRel(),
            linkTo(methodOn(ColoniaController.class).listarTodas()).withRel("colonias")
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar colônia por ID")
    public ResponseEntity<ColoniaResponse> buscarPorId(@PathVariable Long id) {
        ColoniaResponse response = coloniaService.buscarPorId(id);
        response.add(
            linkTo(methodOn(ColoniaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ColoniaController.class).listarTodas()).withRel("colonias"),
            linkTo(methodOn(ColoniaController.class).simularDia(id)).withRel("simular-dia")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar nova colônia")
    public ResponseEntity<ColoniaResponse> criar(@Valid @RequestBody ColoniaRequest request) {
        ColoniaResponse response = coloniaService.criar(request);
        response.add(
            linkTo(methodOn(ColoniaController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(ColoniaController.class).listarTodas()).withRel("colonias")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar colônia")
    public ResponseEntity<ColoniaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ColoniaRequest request) {
        ColoniaResponse response = coloniaService.atualizar(id, request);
        response.add(
            linkTo(methodOn(ColoniaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ColoniaController.class).listarTodas()).withRel("colonias")
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar colônia")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        coloniaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/simular-dia")
    @Operation(summary = "Simular consumo diário de recursos da colônia")
    public ResponseEntity<String> simularDia(@PathVariable Long id) {
        recursoService.simularDia(id);
        return ResponseEntity.ok("Simulação do dia executada com sucesso para a colônia " + id);
    }
}
