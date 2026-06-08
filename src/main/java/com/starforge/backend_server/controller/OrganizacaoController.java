package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.service.OrganizacaoService;
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
@RequestMapping("/v1/organizacoes")
@RequiredArgsConstructor
@Tag(name = "Organizações", description = "Organizações parceiras das missões")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    @GetMapping
    @Operation(summary = "Listar organizações")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de organizações retornada com sucesso")
    })
    public ResponseEntity<CollectionModel<EntityModel<InstituicaoResponse>>> listar() {
        List<EntityModel<InstituicaoResponse>> lista = organizacaoService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(OrganizacaoController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar organização por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organização encontrada"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> buscar(
            @Parameter(description = "ID da organização") @PathVariable String id) {
        return ResponseEntity.ok(toModel(organizacaoService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar organização")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organização criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> criar(@Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(organizacaoService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar organização")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organização atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada")
    })
    public ResponseEntity<EntityModel<InstituicaoResponse>> atualizar(
            @Parameter(description = "ID da organização") @PathVariable String id,
            @Valid @RequestBody InstituicaoRequest request) {
        return ResponseEntity.ok(toModel(organizacaoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover organização")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Organização removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Organização não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da organização") @PathVariable String id) {
        organizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<InstituicaoResponse> toModel(InstituicaoResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(OrganizacaoController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(OrganizacaoController.class).listar()).withRel("organizacoes"));
    }
}
