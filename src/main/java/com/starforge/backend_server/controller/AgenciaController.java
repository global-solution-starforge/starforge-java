package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.service.AgenciaService;
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
@RequestMapping("/v1/agencias")
@RequiredArgsConstructor
@Tag(name = "Agências", description = "Agências espaciais parceiras das missões")
public class AgenciaController {

    private final AgenciaService agenciaService;

    @GetMapping
    @Operation(summary = "Listar agências")
    public ResponseEntity<CollectionModel<EntityModel<InstituicaoResponse>>> listar() {
        List<EntityModel<InstituicaoResponse>> lista = agenciaService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(AgenciaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agência por ID")
    public ResponseEntity<EntityModel<InstituicaoResponse>> buscar(@PathVariable String id) {
        return ResponseEntity.ok(toModel(agenciaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar agência")
    public ResponseEntity<EntityModel<InstituicaoResponse>> criar(@Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(agenciaService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar agência")
    public ResponseEntity<EntityModel<InstituicaoResponse>> atualizar(
            @PathVariable String id, @Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.ok(toModel(agenciaService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover agência")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        agenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<InstituicaoResponse> toModel(InstituicaoResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(AgenciaController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(AgenciaController.class).listar()).withRel("agencias"));
    }
}
