package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.service.AgenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de agências retornada com sucesso")
    })
    public ResponseEntity<CollectionModel<EntityModel<InstituicaoResponse>>> listar() {
        List<EntityModel<InstituicaoResponse>> lista = agenciaService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(AgenciaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agência por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência encontrada"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> buscar(
            @Parameter(description = "ID da agência") @PathVariable String id) {
        return ResponseEntity.ok(toModel(agenciaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar agência")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agência criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> criar(@Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(agenciaService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar agência")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agência atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> atualizar(
            @Parameter(description = "ID da agência") @PathVariable String id,
            @Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.ok(toModel(agenciaService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover agência")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agência removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Agência não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da agência") @PathVariable String id) {
        agenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<InstituicaoResponse> toModel(InstituicaoResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(AgenciaController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(AgenciaController.class).listar()).withRel("agencias"));
    }
}
