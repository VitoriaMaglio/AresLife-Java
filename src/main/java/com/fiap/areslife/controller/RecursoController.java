package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.AbastecerRequest;
import com.fiap.areslife.dto.request.RecursoRequest;
import com.fiap.areslife.entity.Recurso;
import com.fiap.areslife.enums.TipoRecurso;
import com.fiap.areslife.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colonias/{coloniaId}/recursos")
@RequiredArgsConstructor
@Tag(name = "Recursos", description = "Gerenciamento de recursos das colônias")
public class RecursoController {

    private final RecursoService recursoService;

    @GetMapping
    @Operation(summary = "Listar recursos de uma colônia")
    public ResponseEntity<List<Recurso>> listar(@PathVariable Long coloniaId) {
        return ResponseEntity.ok(recursoService.listarPorColonia(coloniaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recurso por ID")
    public ResponseEntity<Recurso> buscarPorId(@PathVariable Long coloniaId, @PathVariable Long id) {
        return ResponseEntity.ok(recursoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar recurso para a colônia")
    public ResponseEntity<Recurso> criar(
            @PathVariable Long coloniaId,
            @Valid @RequestBody RecursoRequest request) {
        return ResponseEntity.status(201).body(recursoService.criar(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar recurso")
    public ResponseEntity<Void> deletar(@PathVariable Long coloniaId, @PathVariable Long id) {
        recursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/abastecer")
    @Operation(summary = "Abastecer recurso")
    public ResponseEntity<Recurso> abastecer(
            @PathVariable Long coloniaId,
            @PathVariable Long id,
            @Valid @RequestBody AbastecerRequest request) {
        return ResponseEntity.ok(recursoService.abastecer(id, request));
    }

    @GetMapping("/{id}/autonomia")
    @Operation(summary = "Calcular autonomia em dias para um tipo de recurso")
    public ResponseEntity<String> autonomia(
            @PathVariable Long coloniaId,
            @PathVariable Long id,
            @RequestParam TipoRecurso tipo) {
        int dias = recursoService.calcularAutonomia(coloniaId, tipo);
        return ResponseEntity.ok("Autonomia estimada: " + dias + " dia(s) para o recurso " + tipo);
    }
}
