package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.nave.NaveRequest;
import com.starforge.backend_server.dto.nave.NaveResponse;
import com.starforge.backend_server.service.NaveService;
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
@RequestMapping("/v1/naves")
@RequiredArgsConstructor
@Tag(name = "Naves", description = "Naves espaciais vinculadas às missões StarForge")
public class NaveController {

    private final NaveService naveService;

    @GetMapping
    @Operation(summary = "Listar todas as naves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de naves retornada com sucesso")
    })
    public ResponseEntity<CollectionModel<EntityModel<NaveResponse>>> listar() {
        List<EntityModel<NaveResponse>> lista = naveService.listar().stream()
                .map(this::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(NaveController.class).listar()).withSelfRel()));
    }

    @GetMapping("/missao/{missaoId}")
    @Operation(summary = "Buscar nave por missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nave encontrada"),
            @ApiResponse(responseCode = "404", description = "Nave ou missão não encontrada")
    })
    public ResponseEntity<EntityModel<NaveResponse>> buscarPorMissao(
            @Parameter(description = "ID da missão") @PathVariable String missaoId) {
        return ResponseEntity.ok(toModel(naveService.buscarPorMissao(missaoId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nave")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Nave criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<NaveResponse>> criar(@Valid @RequestBody NaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(naveService.criar(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar nave")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nave atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Nave não encontrada")
    })
    public ResponseEntity<EntityModel<NaveResponse>> atualizar(
            @Parameter(description = "ID da nave") @PathVariable String id,
            @Valid @RequestBody NaveRequest request) {
        return ResponseEntity.ok(toModel(naveService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover nave")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nave removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Nave não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da nave") @PathVariable String id) {
        naveService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<NaveResponse> toModel(NaveResponse r) {
        return EntityModel.of(r,
                linkTo(methodOn(NaveController.class).buscarPorMissao(r.missaoId())).withSelfRel(),
                linkTo(methodOn(NaveController.class).listar()).withRel("naves"));
    }
}
