package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.nave.NaveRequest;
import com.starforge.backend_server.dto.nave.NaveResponse;
import com.starforge.backend_server.service.NaveService;
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
@RequestMapping("/v1/naves")
@RequiredArgsConstructor
@Tag(name = "Naves", description = "Naves espaciais vinculadas às missões StarForge")
public class NaveController {

    private final NaveService naveService;

    @GetMapping
    @Operation(summary = "Listar todas as naves")
    public ResponseEntity<CollectionModel<EntityModel<NaveResponse>>> listar() {
        List<EntityModel<NaveResponse>> lista = naveService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(NaveController.class).listar()).withSelfRel()));
    }

    @GetMapping("/missao/{missaoId}")
    @Operation(summary = "Buscar nave por missão")
    public ResponseEntity<EntityModel<NaveResponse>> buscarPorMissao(@PathVariable String missaoId) {
        return ResponseEntity.ok(toModel(naveService.buscarPorMissao(missaoId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nave")
    public ResponseEntity<EntityModel<NaveResponse>> criar(@Valid @RequestBody NaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(naveService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar nave")
    public ResponseEntity<EntityModel<NaveResponse>> atualizar(
            @PathVariable String id, @Valid @RequestBody NaveRequest request) {
        return ResponseEntity.ok(toModel(naveService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover nave")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        naveService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<NaveResponse> toModel(NaveResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(NaveController.class).buscarPorMissao(r.missaoId())).withSelfRel(),
                linkTo(methodOn(NaveController.class).listar()).withRel("naves"));
    }
}
