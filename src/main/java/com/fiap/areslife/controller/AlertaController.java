package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.AlertaResolverRequest;
import com.fiap.areslife.dto.response.AlertaResponse;
import com.fiap.areslife.enums.SeveridadeAlerta;
import com.fiap.areslife.enums.StatusAlerta;
import com.fiap.areslife.enums.TipoAlerta;
import com.fiap.areslife.service.AlertaService;
import com.fiap.areslife.service.mapper.AlertaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Gerenciamento de alertas das colônias")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    @Operation(summary = "Listar alertas")
    public ResponseEntity<List<AlertaResponse>> listar(
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) SeveridadeAlerta severidade,
            @RequestParam(required = false) StatusAlerta status) {

        List<AlertaResponse> list = alertaService.listar(coloniaId, severidade, status)
                .stream()
                .map(AlertaMapper::toResponse)
                .toList();

        list.forEach(a -> a.add(
            linkTo(methodOn(AlertaController.class).buscarPorId(a.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<AlertaResponse> buscarPorId(@PathVariable Long id) {
        AlertaResponse response = AlertaMapper.toResponse(alertaService.buscarPorId(id));
        response.add(
            linkTo(methodOn(AlertaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(AlertaController.class).listar(null, null, null)).withRel("alertas")
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/resolver")
    @Operation(summary = "Resolver alerta")
    public ResponseEntity<AlertaResponse> resolver(
            @PathVariable Long id,
            @Valid @RequestBody AlertaResolverRequest request) {
        AlertaResponse response = AlertaMapper.toResponse(alertaService.resolver(id, request));
        response.add(
            linkTo(methodOn(AlertaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(AlertaController.class).listar(null, null, null)).withRel("alertas")
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/colonias/{coloniaId}/resolver-lote")
    @Operation(summary = "Resolver alertas em lote por colônia")
    public ResponseEntity<String> resolverLote(
            @PathVariable Long coloniaId,
            @RequestParam TipoAlerta tipoAlerta) {
        int resolvidos = alertaService.resolverLotePorColonia(coloniaId, tipoAlerta);
        return ResponseEntity.ok(resolvidos + " alerta(s) resolvido(s) com sucesso.");
    }
}
