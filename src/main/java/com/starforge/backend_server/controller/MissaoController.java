package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.missao.MissaoProgressoResponse;
import com.starforge.backend_server.dto.missao.MissaoRequest;
import com.starforge.backend_server.dto.missao.MissaoResponse;
import com.starforge.backend_server.service.MissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v1/missoes")
@RequiredArgsConstructor
@Tag(name = "Missões", description = "CRUD de missões espaciais StarForge")
public class MissaoController {

    private final MissaoService missaoService;

    @GetMapping
    @Operation(summary = "Listar todas as missões")
    public ResponseEntity<CollectionModel<EntityModel<MissaoResponse>>> listar() {
        List<EntityModel<MissaoResponse>> lista = missaoService.listar().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(MissaoController.class).listar()).withSelfRel()));
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar missões ativas")
    public ResponseEntity<CollectionModel<EntityModel<MissaoResponse>>> listarAtivas() {
        List<EntityModel<MissaoResponse>> lista = missaoService.listarAtivas().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(MissaoController.class).listarAtivas()).withSelfRel(),
                linkTo(methodOn(MissaoController.class).listar()).withRel("todas")));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar missão por ID")
    public ResponseEntity<EntityModel<MissaoResponse>> buscar(@PathVariable String id) {
        return ResponseEntity.ok(toModel(missaoService.buscarPorId(id)));
    }

    @GetMapping("/{id}/progresso")
    @Operation(summary = "Progresso de arrecadação da missão")
    public ResponseEntity<EntityModel<MissaoProgressoResponse>> progresso(@PathVariable String id) {
        MissaoProgressoResponse response = missaoService.buscarProgresso(id);
        EntityModel<MissaoProgressoResponse> model = EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).progresso(id)).withSelfRel(),
                linkTo(methodOn(MissaoController.class).buscar(id)).withRel("missao"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nova missão")
    public ResponseEntity<EntityModel<MissaoResponse>> criar(@Valid @RequestBody MissaoRequest request) {
        MissaoResponse response = missaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar missão")
    public ResponseEntity<EntityModel<MissaoResponse>> atualizar(
            @PathVariable String id,
            @Valid @RequestBody MissaoRequest request) {
        return ResponseEntity.ok(toModel(missaoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover missão")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        missaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<MissaoResponse> toModel(MissaoResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(MissaoController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(MissaoController.class).progresso(r.id())).withRel("progresso"),
                linkTo(methodOn(MissaoController.class).listar()).withRel("missoes"));
    }
}
