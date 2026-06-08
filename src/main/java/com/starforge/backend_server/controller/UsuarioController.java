package com.starforge.backend_server.controller;

import com.starforge.backend_server.database.model.Usuario;
import com.starforge.backend_server.dto.usuario.UsuarioAtualizacaoRequest;
import com.starforge.backend_server.dto.usuario.UsuarioResponse;
import com.starforge.backend_server.dto.usuario.UsuarioResumoResponse;
import com.starforge.backend_server.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "CRUD de pilotos da plataforma StarForge")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os pilotos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN")
    })
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponse>>> listar() {
        List<EntityModel<UsuarioResponse>> lista = usuarioService.listar().stream()
                .map(u -> EntityModel.of(u,
                        linkTo(methodOn(UsuarioController.class).buscar(u.id())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}/resumo")
    @Operation(summary = "Resumo do piloto — total contribuído e missões apoiadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas o próprio piloto ou ADMIN"),
            @ApiResponse(responseCode = "404", description = "Piloto não encontrado")
    })
    public ResponseEntity<EntityModel<UsuarioResumoResponse>> resumo(
            @Parameter(description = "ID do piloto") @PathVariable String id) {
        verificarAcesso(id);
        UsuarioResumoResponse response = usuarioService.buscarResumo(id);
        EntityModel<UsuarioResumoResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).resumo(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).buscar(id)).withRel("usuario")
        );
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar piloto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Piloto encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas o próprio piloto ou ADMIN"),
            @ApiResponse(responseCode = "404", description = "Piloto não encontrado")
    })
    public ResponseEntity<EntityModel<UsuarioResponse>> buscar(
            @Parameter(description = "ID do piloto") @PathVariable String id) {
        verificarAcesso(id);
        UsuarioResponse response = usuarioService.buscarPorId(id);
        EntityModel<UsuarioResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).resumo(id)).withRel("resumo"),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")
        );
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do piloto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas o próprio piloto ou ADMIN"),
            @ApiResponse(responseCode = "404", description = "Piloto não encontrado")
    })
    public ResponseEntity<EntityModel<UsuarioResponse>> atualizar(
            @Parameter(description = "ID do piloto") @PathVariable String id,
            @Valid @RequestBody UsuarioAtualizacaoRequest request) {
        verificarAcesso(id);
        UsuarioResponse response = usuarioService.atualizar(id, request);
        EntityModel<UsuarioResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).resumo(id)).withRel("resumo"),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")
        );
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar conta do piloto (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta desativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas o próprio piloto ou ADMIN"),
            @ApiResponse(responseCode = "404", description = "Piloto não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do piloto") @PathVariable String id) {
        verificarAcesso(id);
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Permite acesso se o usuário autenticado for ADMIN ou se o id bater com o próprio id
    private void verificarAcesso(String id) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = autenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !autenticado.getId().equals(id)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar seus próprios dados.");
        }
    }
}
