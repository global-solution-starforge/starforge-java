package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.missao.FaseMissaoAtualizacaoRequest;
import com.starforge.backend_server.dto.missao.FaseMissaoResponse;
import com.starforge.backend_server.dto.missao.MissaoProgressoResponse;
import com.starforge.backend_server.dto.missao.MissaoRequest;
import com.starforge.backend_server.dto.missao.MissaoResponse;
import com.starforge.backend_server.service.MissaoService;
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
@RequestMapping("/v1/missoes")
@RequiredArgsConstructor
@Tag(name = "Missões", description = "CRUD de missões espaciais StarForge")
public class MissaoController {

    private final MissaoService missaoService;

    @GetMapping
    @Operation(summary = "Listar todas as missões")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de missões retornada com sucesso")
    })
    public ResponseEntity<CollectionModel<EntityModel<MissaoResponse>>> listar() {
        List<EntityModel<MissaoResponse>> lista = missaoService.listar().stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(MissaoController.class).listar()).withSelfRel()));
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar missões ativas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de missões ativas retornada com sucesso")
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Missão encontrada"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<EntityModel<MissaoResponse>> buscar(
            @Parameter(description = "ID da missão") @PathVariable String id) {
        return ResponseEntity.ok(toModel(missaoService.buscarPorId(id)));
    }

    @GetMapping("/{id}/progresso")
    @Operation(summary = "Progresso de arrecadação da missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progresso retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<EntityModel<MissaoProgressoResponse>> progresso(
            @Parameter(description = "ID da missão") @PathVariable String id) {
        MissaoProgressoResponse response = missaoService.buscarProgresso(id);
        EntityModel<MissaoProgressoResponse> model = EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).progresso(id)).withSelfRel(),
                linkTo(methodOn(MissaoController.class).buscar(id)).withRel("missao"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nova missão")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Missão criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<MissaoResponse>> criar(@Valid @RequestBody MissaoRequest request) {
        MissaoResponse response = missaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Missão atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<EntityModel<MissaoResponse>> atualizar(
            @Parameter(description = "ID da missão") @PathVariable String id,
            @Valid @RequestBody MissaoRequest request) {
        return ResponseEntity.ok(toModel(missaoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover missão")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Missão removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da missão") @PathVariable String id) {
        missaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/fases")
    @Operation(summary = "Listar fases da missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fases retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<FaseMissaoResponse>>> listarFases(
            @Parameter(description = "ID da missão") @PathVariable String id) {
        List<EntityModel<FaseMissaoResponse>> lista = missaoService.listarFases(id).stream()
                .map(f -> EntityModel.of(f,
                        linkTo(methodOn(MissaoController.class).listarFases(id)).withSelfRel(),
                        linkTo(methodOn(MissaoController.class).buscar(id)).withRel("missao")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(MissaoController.class).listarFases(id)).withSelfRel()));
    }

    @PutMapping("/{id}/fases/{numeroFase}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar fase da missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fase atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Missão ou fase não encontrada")
    })
    public ResponseEntity<EntityModel<FaseMissaoResponse>> atualizarFase(
            @Parameter(description = "ID da missão") @PathVariable String id,
            @Parameter(description = "Número da fase") @PathVariable int numeroFase,
            @Valid @RequestBody FaseMissaoAtualizacaoRequest request) {
        FaseMissaoResponse response = missaoService.atualizarFase(id, numeroFase, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).listarFases(id)).withRel("fases"),
                linkTo(methodOn(MissaoController.class).buscar(id)).withRel("missao")));
    }

    private EntityModel<MissaoResponse> toModel(MissaoResponse r) {
        var model = EntityModel.of(r,
                linkTo(methodOn(MissaoController.class).buscar(r.id())).withSelfRel(),
                linkTo(methodOn(MissaoController.class).progresso(r.id())).withRel("progresso"),
                linkTo(methodOn(MissaoController.class).listarFases(r.id())).withRel("fases"),
                linkTo(methodOn(NaveController.class).buscarPorMissao(r.id())).withRel("nave"),
                linkTo(methodOn(MissaoController.class).listar()).withRel("missoes"));
        return model;
    }
}
