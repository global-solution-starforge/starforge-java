package com.starforge.backend_server.controller;

import com.starforge.backend_server.database.model.StatusUsuario;
import com.starforge.backend_server.database.model.UserRole;
import com.starforge.backend_server.database.model.Usuario;
import com.starforge.backend_server.database.repository.UsuarioRepository;
import com.starforge.backend_server.dto.auth.AuthResponse;
import com.starforge.backend_server.dto.auth.CadastroRequest;
import com.starforge.backend_server.dto.auth.LoginRequest;
import com.starforge.backend_server.dto.usuario.UsuarioResponse;
import com.starforge.backend_server.exception.RegraDeNegocioException;
import com.starforge.backend_server.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Autenticação", description = "Registro e login de pilotos")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Login do piloto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso — JWT retornado"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var usuario = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(new AuthResponse(
                tokenService.generateToken(usuario),
                usuario.getId()
        ));
    }

    @PostMapping("/register")
    @Operation(summary = "Alistamento Orbital — cadastrar novo piloto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Piloto registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: email já cadastrado)")
    })
    public ResponseEntity<EntityModel<UsuarioResponse>> registrar(@Valid @RequestBody CadastroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Email já cadastrado: " + request.email());
        }

        var usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setRole(request.role() != null ? request.role() : UserRole.USER);
        usuario.setStatus(StatusUsuario.ATIVO);
        usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getStatus().name(), usuario.getRole().name(), usuario.getDataCadastro());
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).buscar(usuario.getId())).withRel("perfil"),
                linkTo(methodOn(UsuarioController.class).resumo(usuario.getId())).withRel("resumo")));
    }
}
