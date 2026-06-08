package com.starforge.backend_server.controller;

import com.starforge.backend_server.database.model.Usuario;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoRequest;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoResponse;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoStatusRequest;
import com.starforge.backend_server.service.ContribuicaoService;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v1/contribuicoes")
@RequiredArgsConstructor
@Tag(name = "Contribuições", description = "Registro de contribuições e vínculo com Hangar")
public class ContribuicaoController {

    private final ContribuicaoService contribuicaoService;

    @PostMapping
    @Operation(summary = "Criar contribuição (gera entrada no Hangar automaticamente)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contribuição criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    public ResponseEntity<EntityModel<ContribuicaoResponse>> criar(@Valid @RequestBody ContribuicaoRequest request) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ContribuicaoResponse response = contribuicaoService.criar(autenticado.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Listar contribuições de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contribuições retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas o próprio piloto ou ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<CollectionModel<EntityModel<ContribuicaoResponse>>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable String id) {
        verificarAcessoUsuario(id);
        List<EntityModel<ContribuicaoResponse>> lista = contribuicaoService.listarPorUsuario(id).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(ContribuicaoController.class).listarPorUsuario(id)).withSelfRel()));
    }

    @GetMapping("/missao/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar contribuições de uma missão")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contribuições retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Missão não encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ContribuicaoResponse>>> listarPorMissao(
            @Parameter(description = "ID da missão") @PathVariable String id) {
        List<EntityModel<ContribuicaoResponse>> lista = contribuicaoService.listarPorMissao(id).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(ContribuicaoController.class).listarPorMissao(id)).withSelfRel()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar status de uma contribuição")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Contribuição não encontrada")
    })
    public ResponseEntity<EntityModel<ContribuicaoResponse>> atualizarStatus(
            @Parameter(description = "ID da contribuição") @PathVariable String id,
            @Valid @RequestBody ContribuicaoStatusRequest request) {
        return ResponseEntity.ok(toModel(contribuicaoService.atualizarStatus(id, request)));
    }

    private void verificarAcessoUsuario(String id) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = autenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !autenticado.getId().equals(id)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar suas próprias contribuições.");
        }
    }

    private EntityModel<ContribuicaoResponse> toModel(ContribuicaoResponse r) {
        var model = EntityModel.of(r,
                linkTo(methodOn(ContribuicaoController.class).listarPorUsuario(r.usuarioId())).withSelfRel(),
                linkTo(methodOn(ContribuicaoController.class).listarPorUsuario(r.usuarioId())).withRel("contribuicoes-usuario"),
                linkTo(methodOn(ContribuicaoController.class).listarPorMissao(r.missaoId())).withRel("contribuicoes-missao"),
                linkTo(methodOn(MissaoController.class).buscar(r.missaoId())).withRel("missao"));
        if (r.hangarId() != null) {
            model.add(linkTo(methodOn(HangarController.class).listarPorUsuario(r.usuarioId())).withRel("hangar"));
        }
        return model;
    }
}
