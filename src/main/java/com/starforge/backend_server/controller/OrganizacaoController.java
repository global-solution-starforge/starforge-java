package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.service.OrganizacaoService;
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
@RequestMapping("/v1/organizacoes")
@RequiredArgsConstructor
@Tag(name = "Organizações", description = "Organizações parceiras das missões")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    @GetMapping
    @Operation(summary = "Listar organizações")
    public ResponseEntity<CollectionModel<EntityModel<InstituicaoResponse>>> listar() {
        List<EntityModel<InstituicaoResponse>> lista = organizacaoService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(OrganizacaoController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar organização por ID")
    public ResponseEntity<EntityModel<InstituicaoResponse>> buscar(@PathVariable String id) {
        return ResponseEntity.ok(toModel(organizacaoService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar organização")
    public ResponseEntity<EntityModel<InstituicaoResponse>> criar(@Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(organizacaoService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar organização")
    public ResponseEntity<EntityModel<InstituicaoResponse>> atualizar(
            @PathVariable String id, @Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.ok(toModel(organizacaoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover organização")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        organizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<InstituicaoResponse> toModel(InstituicaoResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(OrganizacaoController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(OrganizacaoController.class).listar()).withRel("organizacoes"));
    }
}
