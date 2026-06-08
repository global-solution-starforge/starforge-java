package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.*;
import com.starforge.backend_server.database.repository.*;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoRequest;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoResponse;
import com.starforge.backend_server.dto.contribuicao.ContribuicaoStatusRequest;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import com.starforge.backend_server.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContribuicaoService {

    private final ContribuicaoRepository contribuicaoRepository;
    private final HangarRepository hangarRepository;
    private final UsuarioRepository usuarioRepository;
    private final MissaoRepository missaoRepository;
    private final TierRepository tierRepository;

    @Transactional
    public ContribuicaoResponse criar(String usuarioId, ContribuicaoRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));

        Missao missao = missaoRepository.findById(request.missaoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Missão não encontrada: " + request.missaoId()));

        if (missao.getStatus() != StatusMissao.ATIVA) {
            throw new RegraDeNegocioException("Só é possível contribuir com missões ativas.");
        }

        Tier tier = tierRepository.findById(request.tierId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Tier não encontrado: " + request.tierId()));

        if (request.valor().compareTo(tier.getValorMinimo()) < 0) {
            throw new RegraDeNegocioException(
                    "Valor mínimo para o tier " + tier.getNome() + " é R$ " + tier.getValorMinimo());
        }

        Contribuicao contribuicao = new Contribuicao();
        contribuicao.setId(UUID.randomUUID().toString());
        contribuicao.setValor(request.valor());
        contribuicao.setStatus(StatusContribuicao.PENDENTE);
        contribuicao.setMetodoPagamento(request.metodoPagamento());
        contribuicao.setDataContribuicao(LocalDate.now());
        contribuicao.setUsuario(usuario);
        contribuicao.setMissao(missao);
        contribuicao.setTier(tier);
        contribuicaoRepository.save(contribuicao);

        // Cria entrada no Hangar vinculada à nave da missão (se existir)
        String hangarId = null;
        if (missao.getNave() != null) {
            Hangar hangar = new Hangar();
            hangar.setId(UUID.randomUUID().toString());
            hangar.setStatus(StatusHangar.PENDENTE);
            hangar.setNave(missao.getNave());
            hangar.setContribuicao(contribuicao);
            hangarRepository.save(hangar);
            hangarId = hangar.getId();
        }

        return toResponse(contribuicao, hangarId);
    }

    public List<ContribuicaoResponse> listarPorUsuario(String usuarioId) {
        return contribuicaoRepository.findByUsuarioId(usuarioId).stream()
                .map(c -> toResponse(c, c.getHangar() != null ? c.getHangar().getId() : null))
                .toList();
    }

    public List<ContribuicaoResponse> listarPorMissao(String missaoId) {
        return contribuicaoRepository.findByMissaoId(missaoId).stream()
                .map(c -> toResponse(c, c.getHangar() != null ? c.getHangar().getId() : null))
                .toList();
    }

    public ContribuicaoResponse atualizarStatus(String id, ContribuicaoStatusRequest request) {
        Contribuicao contribuicao = contribuicaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contribuição não encontrada: " + id));
        contribuicao.setStatus(request.status());
        contribuicaoRepository.save(contribuicao);
        return toResponse(contribuicao, contribuicao.getHangar() != null ? contribuicao.getHangar().getId() : null);
    }

    private ContribuicaoResponse toResponse(Contribuicao c, String hangarId) {
        return new ContribuicaoResponse(
                c.getId(),
                c.getValor(),
                c.getStatus().name(),
                c.getMetodoPagamento().name(),
                c.getDataContribuicao(),
                c.getUsuario().getId(),
                c.getUsuario().getNome(),
                c.getMissao().getId(),
                c.getMissao().getNome(),
                c.getTier().getId(),
                c.getTier().getNome().name(),
                hangarId
        );
    }
}
