package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.StatusContribuicao;
import com.starforge.backend_server.database.model.StatusUsuario;
import com.starforge.backend_server.database.model.UserRole;
import com.starforge.backend_server.database.model.Usuario;
import com.starforge.backend_server.database.repository.ContribuicaoRepository;
import com.starforge.backend_server.database.repository.UsuarioRepository;
import com.starforge.backend_server.dto.usuario.UsuarioAtualizacaoRequest;
import com.starforge.backend_server.dto.usuario.UsuarioCriacaoRequest;
import com.starforge.backend_server.dto.usuario.UsuarioResponse;
import com.starforge.backend_server.dto.usuario.UsuarioResumoResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import com.starforge.backend_server.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContribuicaoRepository contribuicaoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse criar(UsuarioCriacaoRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Email já cadastrado: " + request.email());
        }
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setRole(UserRole.USER);
        usuario.setStatus(StatusUsuario.ATIVO);
        return toResponse(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(String id) {
        return toResponse(buscarEntidade(id));
    }

    public UsuarioResponse atualizar(String id, UsuarioAtualizacaoRequest request) {
        Usuario usuario = buscarEntidade(id);

        if (request.nome() != null) {
            usuario.setNome(request.nome());
        }
        if (request.email() != null && !request.email().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.email())) {
                throw new RegraDeNegocioException("Email já cadastrado: " + request.email());
            }
            usuario.setEmail(request.email());
        }
        if (request.senha() != null) {
            usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        }

        return toResponse(usuarioRepository.save(usuario));
    }

    public void deletar(String id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setStatus(StatusUsuario.INATIVO);
        usuarioRepository.save(usuario);
    }

    public UsuarioResumoResponse buscarResumo(String id) {
        buscarEntidade(id);
        var total = contribuicaoRepository.sumValorByUsuarioIdAndStatus(id, StatusContribuicao.CONFIRMADO);
        var missoes = contribuicaoRepository.countMissoesDistinctByUsuarioIdAndStatus(id, StatusContribuicao.CONFIRMADO);
        return new UsuarioResumoResponse(id, total, missoes);
    }

    public Usuario buscarEntidade(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getStatus().name(),
                u.getRole().name(),
                u.getDataCadastro()
        );
    }
}
