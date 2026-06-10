package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Hangar;
import com.starforge.backend_server.database.model.StatusHangar;
import com.starforge.backend_server.database.repository.HangarRepository;
import com.starforge.backend_server.dto.hangar.HangarDesbloquearRequest;
import com.starforge.backend_server.dto.hangar.HangarResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import com.starforge.backend_server.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HangarService {

    private final HangarRepository hangarRepository;

    public List<HangarResponse> listarPorUsuario(String usuarioId) {
        return hangarRepository.findByContribuicao_UsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    public HangarResponse desbloquear(String usuarioId, HangarDesbloquearRequest request) {
        Hangar hangar = hangarRepository.findById(request.hangarId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Hangar não encontrado: " + request.hangarId()));

        if (!hangar.getContribuicao().getUsuario().getId().equals(usuarioId)) {
            throw new RegraDeNegocioException("Este hangar não pertence ao usuário autenticado.");
        }

        if (hangar.getStatus() == StatusHangar.DESBLOQUEADA) {
            throw new RegraDeNegocioException("Hangar já está desbloqueado.");
        }

        hangar.setStatus(StatusHangar.DESBLOQUEADA);
        hangar.setDataDesbloqueio(LocalDate.now());
        if (request.nomeGravado() != null) {
            hangar.setNomeGravado(request.nomeGravado());
        }

        return toResponse(hangarRepository.save(hangar));
    }

    @Transactional
    public void deletar(String id, String usuarioId, boolean isAdmin) {
        Hangar hangar = hangarRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Hangar não encontrado: " + id));

        if (!isAdmin && !hangar.getContribuicao().getUsuario().getId().equals(usuarioId)) {
            throw new RegraDeNegocioException("Este hangar não pertence ao usuário autenticado.");
        }

        hangarRepository.delete(hangar);
    }

    private HangarResponse toResponse(Hangar h) {
        String naveId = h.getNave() != null ? h.getNave().getId() : null;
        String naveNome = h.getNave() != null ? h.getNave().getNome() : null;
        String contribuicaoId = h.getContribuicao() != null ? h.getContribuicao().getId() : null;
        String missaoNome = h.getContribuicao() != null && h.getContribuicao().getMissao() != null
                ? h.getContribuicao().getMissao().getNome() : null;
        String usuarioId = h.getContribuicao() != null && h.getContribuicao().getUsuario() != null
                ? h.getContribuicao().getUsuario().getId() : null;
        return new HangarResponse(
                h.getId(), h.getStatus().name(), h.getDataDesbloqueio(),
                h.getNomeGravado(), naveId, naveNome, contribuicaoId, missaoNome, usuarioId
        );
    }
}
