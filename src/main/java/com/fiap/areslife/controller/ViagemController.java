package com.fiap.areslife.controller;

import com.fiap.areslife.dto.request.ViagemRequest;
import com.fiap.areslife.dto.response.ViagemResponse;
import com.fiap.areslife.enums.StatusViagem;
import com.fiap.areslife.service.ViagemService;
import com.fiap.areslife.service.mapper.ViagemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/viagens")
@RequiredArgsConstructor
@Tag(name = "Viagens Turísticas", description = "Gerenciamento de viagens turísticas espaciais")
public class ViagemController {

    private final ViagemService viagemService;

    @GetMapping
    @Operation(summary = "Listar viagens (filtros: habitanteId, status)")
    public ResponseEntity<List<ViagemResponse>> listar(
            @RequestParam(required = false) Long habitanteId,
            @RequestParam(required = false) StatusViagem status) {

        List<ViagemResponse> list = viagemService.listar(habitanteId, status)
                .stream()
                .map(ViagemMapper::toResponse)
                .toList();

        list.forEach(v -> v.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(v.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar viagem por ID")
    public ResponseEntity<ViagemResponse> buscarPorId(@PathVariable Long id) {
        ViagemResponse response = ViagemMapper.toResponse(viagemService.buscarPorId(id));
        response.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ViagemController.class).listar(null, null)).withRel("viagens"),
            linkTo(methodOn(ViagemController.class).iniciar(id)).withRel("iniciar"),
            linkTo(methodOn(ViagemController.class).concluir(id)).withRel("concluir"),
            linkTo(methodOn(ViagemController.class).cancelar(id)).withRel("cancelar")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Reservar nova viagem turística")
    public ResponseEntity<ViagemResponse> reservar(@Valid @RequestBody ViagemRequest request) {
        ViagemResponse response = ViagemMapper.toResponse(viagemService.reservar(request));
        response.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(response.getId())).withSelfRel(),
            linkTo(methodOn(ViagemController.class).listar(null, null)).withRel("viagens")
        );
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar viagem")
    public ResponseEntity<ViagemResponse> iniciar(@PathVariable Long id) {
        ViagemResponse response = ViagemMapper.toResponse(viagemService.iniciar(id));
        response.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ViagemController.class).concluir(id)).withRel("concluir")
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir viagem")
    public ResponseEntity<ViagemResponse> concluir(@PathVariable Long id) {
        ViagemResponse response = ViagemMapper.toResponse(viagemService.concluir(id));
        response.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ViagemController.class).listar(null, null)).withRel("viagens")
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar viagem")
    public ResponseEntity<ViagemResponse> cancelar(@PathVariable Long id) {
        ViagemResponse response = ViagemMapper.toResponse(viagemService.cancelar(id));
        response.add(
            linkTo(methodOn(ViagemController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ViagemController.class).listar(null, null)).withRel("viagens")
        );
        return ResponseEntity.ok(response);
    }
}
