package com.starforge.backend_server.controller;

import com.starforge.backend_server.dto.usuario.UsuarioAtualizacaoRequest;
import com.starforge.backend_server.dto.usuario.UsuarioCriacaoRequest;
import com.starforge.backend_server.dto.usuario.UsuarioResponse;
import com.starforge.backend_server.service.UsuarioService;
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
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "CRUD de pilotos da plataforma StarForge")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar novo piloto")
    public ResponseEntity<EntityModel<UsuarioResponse>> criar(@Valid @RequestBody UsuarioCriacaoRequest request) {
        UsuarioResponse response = usuarioService.criar(request);
        EntityModel<UsuarioResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(response.id())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os pilotos")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponse>>> listar() {
        List<EntityModel<UsuarioResponse>> lista = usuarioService.listar().stream()
                .map(u -> EntityModel.of(u,
                        linkTo(methodOn(UsuarioController.class).buscar(u.id())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar piloto por ID")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscar(@PathVariable String id) {
        UsuarioResponse response = usuarioService.buscarPorId(id);
        EntityModel<UsuarioResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")
        );
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do piloto")
    public ResponseEntity<EntityModel<UsuarioResponse>> atualizar(
            @PathVariable String id,
            @Valid @RequestBody UsuarioAtualizacaoRequest request) {
        UsuarioResponse response = usuarioService.atualizar(id, request);
        EntityModel<UsuarioResponse> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")
        );
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar conta do piloto (soft delete)")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
