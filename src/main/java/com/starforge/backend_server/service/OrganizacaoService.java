package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Organizacao;
import com.starforge.backend_server.database.repository.OrganizacaoRepository;
import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;

    public List<InstituicaoResponse> listar() {
        return organizacaoRepository.findAll().stream()
                .map(o -> new InstituicaoResponse(o.getId(), o.getNome()))
                .toList();
    }

    public InstituicaoResponse buscarPorId(String id) {
        Organizacao org = buscarEntidade(id);
        return new InstituicaoResponse(org.getId(), org.getNome());
    }

    public InstituicaoResponse criar(InstituicaoRequest request) {
        Organizacao org = new Organizacao(UUID.randomUUID().toString(), request.nome());
        organizacaoRepository.save(org);
        return new InstituicaoResponse(org.getId(), org.getNome());
    }

    public InstituicaoResponse atualizar(String id, InstituicaoRequest request) {
        Organizacao org = buscarEntidade(id);
        org.setNome(request.nome());
        organizacaoRepository.save(org);
        return new InstituicaoResponse(org.getId(), org.getNome());
    }

    public void deletar(String id) {
        organizacaoRepository.delete(buscarEntidade(id));
    }

    public Organizacao buscarEntidade(String id) {
        return organizacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Organização não encontrada: " + id));
    }
}
