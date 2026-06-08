package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Tier;
import com.starforge.backend_server.database.repository.TierRepository;
import com.starforge.backend_server.dto.tier.TierRequest;
import com.starforge.backend_server.dto.tier.TierResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TierService {

    private final TierRepository tierRepository;

    public List<TierResponse> listar() {
        return tierRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TierResponse buscarPorId(String id) {
        return toResponse(buscarEntidade(id));
    }

    public TierResponse criar(TierRequest request) {
        Tier tier = new Tier();
        tier.setId(UUID.randomUUID().toString());
        aplicarRequest(tier, request);
        return toResponse(tierRepository.save(tier));
    }

    public TierResponse atualizar(String id, TierRequest request) {
        Tier tier = buscarEntidade(id);
        aplicarRequest(tier, request);
        return toResponse(tierRepository.save(tier));
    }

    public void deletar(String id) {
        tierRepository.delete(buscarEntidade(id));
    }

    public Tier buscarEntidade(String id) {
        return tierRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Tier não encontrado: " + id));
    }

    private void aplicarRequest(Tier tier, TierRequest req) {
        tier.setNome(req.nome());
        tier.setValorMinimo(req.valorMinimo());
        tier.setFlagAcessoAntecipado(Boolean.TRUE.equals(req.acessoAntecipado()) ? 'S' : 'N');
        tier.setDescricao(req.descricao());
    }

    private TierResponse toResponse(Tier t) {
        return new TierResponse(
                t.getId(),
                t.getNome().name(),
                t.getValorMinimo(),
                t.getFlagAcessoAntecipado() == 'S',
                t.getDescricao()
        );
    }
}
