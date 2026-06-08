package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Missao;
import com.starforge.backend_server.database.model.Nave;
import com.starforge.backend_server.database.repository.MissaoRepository;
import com.starforge.backend_server.database.repository.NaveRepository;
import com.starforge.backend_server.dto.nave.NaveRequest;
import com.starforge.backend_server.dto.nave.NaveResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import com.starforge.backend_server.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaveService {

    private final NaveRepository naveRepository;
    private final MissaoRepository missaoRepository;

    public List<NaveResponse> listar() {
        return naveRepository.findAll().stream().map(this::toResponse).toList();
    }

    public NaveResponse buscarPorMissao(String missaoId) {
        Nave nave = naveRepository.findByMissaoId(missaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nenhuma nave encontrada para a missão: " + missaoId));
        return toResponse(nave);
    }

    public NaveResponse criar(NaveRequest request) {
        Missao missao = buscarMissao(request.missaoId());

        if (naveRepository.findByMissaoId(request.missaoId()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma nave associada à missão: " + request.missaoId());
        }

        Nave nave = new Nave();
        nave.setId(UUID.randomUUID().toString());
        nave.setNome(request.nome());
        nave.setClasse(request.classe());
        nave.setImagemUrl(request.imagemUrl());
        nave.setMissao(missao);
        return toResponse(naveRepository.save(nave));
    }

    public NaveResponse atualizar(String id, NaveRequest request) {
        Nave nave = buscarEntidade(id);
        Missao missao = buscarMissao(request.missaoId());

        // Impede trocar para uma missão que já tem outra nave
        naveRepository.findByMissaoId(request.missaoId()).ifPresent(outra -> {
            if (!outra.getId().equals(id)) {
                throw new RegraDeNegocioException("Já existe uma nave associada à missão: " + request.missaoId());
            }
        });

        nave.setNome(request.nome());
        nave.setClasse(request.classe());
        nave.setImagemUrl(request.imagemUrl());
        nave.setMissao(missao);
        return toResponse(naveRepository.save(nave));
    }

    public void deletar(String id) {
        naveRepository.delete(buscarEntidade(id));
    }

    private Nave buscarEntidade(String id) {
        return naveRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nave não encontrada: " + id));
    }

    private Missao buscarMissao(String missaoId) {
        return missaoRepository.findById(missaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Missão não encontrada: " + missaoId));
    }

    private NaveResponse toResponse(Nave n) {
        return new NaveResponse(
                n.getId(), n.getNome(), n.getClasse(), n.getImagemUrl(),
                n.getMissao().getId(), n.getMissao().getNome()
        );
    }
}
