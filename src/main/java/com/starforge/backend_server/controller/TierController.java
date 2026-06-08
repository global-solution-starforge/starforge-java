package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.tier.TierRequest;
import com.starforge.backend_server.dto.tier.TierResponse;
import com.starforge.backend_server.service.TierService;
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
@RequestMapping("/v1/tiers")
@RequiredArgsConstructor
@Tag(name = "Tiers", description = "Níveis de contribuição da plataforma StarForge")
public class TierController {

    private final TierService tierService;

    @GetMapping
    @Operation(summary = "Listar todos os tiers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tiers retornada com sucesso")
    })
    public ResponseEntity<CollectionModel<EntityModel<TierResponse>>> listar() {
        List<EntityModel<TierResponse>> lista = tierService.listar().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(TierController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tier por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tier encontrado"),
            @ApiResponse(responseCode = "404", description = "Tier não encontrado")
    })
    public ResponseEntity<EntityModel<TierResponse>> buscar(
            @Parameter(description = "ID do tier") @PathVariable String id) {
        return ResponseEntity.ok(toModel(tierService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar tier")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tier criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<TierResponse>> criar(@Valid @RequestBody TierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(tierService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar tier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tier atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Tier não encontrado")
    })
    public ResponseEntity<EntityModel<TierResponse>> atualizar(
            @Parameter(description = "ID do tier") @PathVariable String id,
            @Valid @RequestBody TierRequest request) {
        return ResponseEntity.ok(toModel(tierService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover tier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tier removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Tier não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do tier") @PathVariable String id) {
        tierService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<TierResponse> toModel(TierResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(TierController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(TierController.class).listar()).withRel("tiers"));
    }
}
