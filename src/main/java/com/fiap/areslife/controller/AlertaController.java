package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.AlertaResolverRequest;
import com.fiap.areslife.entity.Alerta;
import com.fiap.areslife.enums.SeveridadeAlerta;
import com.fiap.areslife.enums.StatusAlerta;
import com.fiap.areslife.enums.TipoAlerta;
import com.fiap.areslife.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Gerenciamento de alertas das colônias")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    @Operation(summary = "Listar alertas (filtros: coloniaId, severidade, status)")
    public ResponseEntity<List<Alerta>> listar(
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) SeveridadeAlerta severidade,
            @RequestParam(required = false) StatusAlerta status) {
        return ResponseEntity.ok(alertaService.listar(coloniaId, severidade, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.buscarPorId(id));
    }

    @PatchMapping("/{id}/resolver")
    @Operation(summary = "Resolver alerta")
    public ResponseEntity<Alerta> resolver(
            @PathVariable Long id,
            @Valid @RequestBody AlertaResolverRequest request) {
        return ResponseEntity.ok(alertaService.resolver(id, request));
    }

    @PatchMapping("/colonias/{coloniaId}/resolver-lote")
    @Operation(summary = "Resolver em lote todos os alertas de um tipo em uma colônia")
    public ResponseEntity<String> resolverLote(
            @PathVariable Long coloniaId,
            @RequestParam TipoAlerta tipoAlerta) {
        int resolvidos = alertaService.resolverLotePorColonia(coloniaId, tipoAlerta);
        return ResponseEntity.ok(resolvidos + " alerta(s) resolvido(s) com sucesso.");
    }
}
