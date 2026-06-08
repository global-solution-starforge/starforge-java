package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Agencia;
import com.starforge.backend_server.database.repository.AgenciaRepository;
import com.starforge.backend_server.dto.instituicao.InstituicaoRequest;
import com.starforge.backend_server.dto.instituicao.InstituicaoResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    public List<InstituicaoResponse> listar() {
        return agenciaRepository.findAll().stream()
                .map(a -> new InstituicaoResponse(a.getId(), a.getNome()))
                .toList();
    }

    public InstituicaoResponse buscarPorId(String id) {
        Agencia agencia = buscarEntidade(id);
        return new InstituicaoResponse(agencia.getId(), agencia.getNome());
    }

    public InstituicaoResponse criar(InstituicaoRequest request) {
        Agencia agencia = new Agencia(UUID.randomUUID().toString(), request.nome());
        agenciaRepository.save(agencia);
        return new InstituicaoResponse(agencia.getId(), agencia.getNome());
    }

    public InstituicaoResponse atualizar(String id, InstituicaoRequest request) {
        Agencia agencia = buscarEntidade(id);
        agencia.setNome(request.nome());
        agenciaRepository.save(agencia);
        return new InstituicaoResponse(agencia.getId(), agencia.getNome());
    }

    public void deletar(String id) {
        agenciaRepository.delete(buscarEntidade(id));
    }

    public Agencia buscarEntidade(String id) {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada: " + id));
    }
}
