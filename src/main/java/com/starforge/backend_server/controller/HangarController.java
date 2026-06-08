package com.starforge.backend_server.controller;

import com.starforge.backend_server.database.model.Usuario;
import com.starforge.backend_server.dto.hangar.HangarDesbloquearRequest;
import com.starforge.backend_server.dto.hangar.HangarResponse;
import com.starforge.backend_server.service.HangarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v1/hangar")
@RequiredArgsConstructor
@Tag(name = "Hangar", description = "Gerenciamento do hangar de naves do piloto")
public class HangarController {

    private final HangarService hangarService;

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Listar hangar de um piloto")
    public ResponseEntity<CollectionModel<EntityModel<HangarResponse>>> listarPorUsuario(@PathVariable String id) {
        verificarAcessoUsuario(id);
        List<EntityModel<HangarResponse>> lista = hangarService.listarPorUsuario(id).stream()
                .map(this::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(HangarController.class).listarPorUsuario(id)).withSelfRel()));
    }

    @PostMapping("/desbloquear")
    @Operation(summary = "Desbloquear nave no hangar")
    public ResponseEntity<EntityModel<HangarResponse>> desbloquear(@Valid @RequestBody HangarDesbloquearRequest request) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HangarResponse response = hangarService.desbloquear(autenticado.getId(), request);
        return ResponseEntity.ok(toModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover entrada do hangar")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = autenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        hangarService.deletar(id, autenticado.getId(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    private void verificarAcessoUsuario(String id) {
        var autenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = autenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !autenticado.getId().equals(id)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar seu próprio hangar.");
        }
    }

    private EntityModel<HangarResponse> toModel(HangarResponse r) {
        var model = EntityModel.of(r,
                linkTo(methodOn(HangarController.class).listarPorUsuario(r.usuarioId())).withSelfRel(),
                linkTo(methodOn(HangarController.class).listarPorUsuario(r.usuarioId())).withRel("hangar-usuario"));
        if ("PENDENTE".equals(r.status())) {
            model.add(linkTo(methodOn(HangarController.class).desbloquear(null)).withRel("desbloquear"));
        }
        return model;
    }
}
