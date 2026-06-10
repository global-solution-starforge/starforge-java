package com.starforge.backend_server.service;

import com.starforge.backend_server.database.model.Agencia;
import com.starforge.backend_server.database.model.FaseMissao;
import com.starforge.backend_server.database.model.Missao;
import com.starforge.backend_server.database.model.Organizacao;
import com.starforge.backend_server.database.model.StatusContribuicao;
import com.starforge.backend_server.database.model.StatusMissao;
import com.starforge.backend_server.database.model.embedded.Coordenadas;
import com.starforge.backend_server.database.repository.AgenciaRepository;
import com.starforge.backend_server.database.repository.ContribuicaoRepository;
import com.starforge.backend_server.database.repository.FaseMissaoRepository;
import com.starforge.backend_server.database.repository.MissaoRepository;
import com.starforge.backend_server.database.repository.OrganizacaoRepository;
import com.starforge.backend_server.dto.missao.FaseMissaoAtualizacaoRequest;
import com.starforge.backend_server.dto.missao.FaseMissaoResponse;
import com.starforge.backend_server.dto.missao.MissaoProgressoResponse;
import com.starforge.backend_server.dto.missao.MissaoRequest;
import com.starforge.backend_server.dto.missao.MissaoResponse;
import com.starforge.backend_server.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final AgenciaRepository agenciaRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final ContribuicaoRepository contribuicaoRepository;
    private final FaseMissaoRepository faseMissaoRepository;

    public List<MissaoResponse> listar() {
        return missaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<MissaoResponse> listarAtivas() {
        return missaoRepository.findByStatus(StatusMissao.ATIVA).stream().map(this::toResponse).toList();
    }

    public MissaoResponse buscarPorId(String id) {
        return toResponse(buscarEntidade(id));
    }

    public MissaoProgressoResponse buscarProgresso(String id) {
        Missao missao = buscarEntidade(id);
        BigDecimal arrecadado = contribuicaoRepository.sumValorByMissaoIdAndStatus(id, StatusContribuicao.CONFIRMADO);
        BigDecimal meta = missao.getValorMeta();
        BigDecimal percentual = meta.compareTo(BigDecimal.ZERO) > 0
                ? arrecadado.multiply(BigDecimal.valueOf(100)).divide(meta, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        return new MissaoProgressoResponse(id, missao.getNome(), meta, arrecadado, percentual);
    }

    public MissaoResponse criar(MissaoRequest request) {
        Missao missao = new Missao();
        missao.setId(UUID.randomUUID().toString());
        aplicarRequest(missao, request);
        if (missao.getStatus() == null) {
            missao.setStatus(StatusMissao.CRIACAO);
        }
        return toResponse(missaoRepository.save(missao));
    }

    public MissaoResponse atualizar(String id, MissaoRequest request) {
        Missao missao = buscarEntidade(id);
        aplicarRequest(missao, request);
        return toResponse(missaoRepository.save(missao));
    }

    @Transactional
    public void deletar(String id) {
        missaoRepository.delete(buscarEntidade(id));
    }

    public List<FaseMissaoResponse> listarFases(String missaoId) {
        buscarEntidade(missaoId);
        return faseMissaoRepository.findByIdMissaoIdOrderByIdNumeroFase(missaoId).stream()
                .map(this::toFaseResponse)
                .toList();
    }

    public FaseMissaoResponse atualizarFase(String missaoId, int numeroFase, FaseMissaoAtualizacaoRequest request) {
        FaseMissao fase = faseMissaoRepository.findByIdMissaoIdAndIdNumeroFase(missaoId, numeroFase)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Fase " + numeroFase + " não encontrada na missão: " + missaoId));
        fase.setNome(request.nome());
        fase.setDescricao(request.descricao());
        fase.setStatus(request.status());
        fase.setPorcentagem(request.porcentagem());
        return toFaseResponse(faseMissaoRepository.save(fase));
    }

    private FaseMissaoResponse toFaseResponse(FaseMissao f) {
        return new FaseMissaoResponse(
                f.getId().getNumeroFase(),
                f.getNome(),
                f.getDescricao(),
                f.getStatus().name(),
                f.getPorcentagem()
        );
    }

    public Missao buscarEntidade(String id) {
        return missaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Missão não encontrada: " + id));
    }

    private void aplicarRequest(Missao missao, MissaoRequest req) {
        missao.setCodigo(req.codigo());
        missao.setNome(req.nome());
        missao.setDescricao(req.descricao());
        if (req.status() != null) missao.setStatus(req.status());
        missao.setValorMeta(req.valorMeta());
        missao.setDataLimite(req.dataLimite());
        missao.setTipoOrbita(req.tipoOrbita());
        missao.setVidaUtil(req.vidaUtil());
        missao.setCargaUtil(req.cargaUtil());
        missao.setBadge(req.badge());

        if (req.latitude() != null || req.longitude() != null) {
            missao.setCoordenadas(new Coordenadas(req.latitude(), req.longitude()));
        }

        if (req.agenciaId() != null) {
            Agencia agencia = agenciaRepository.findById(req.agenciaId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada: " + req.agenciaId()));
            missao.setAgencia(agencia);
        } else {
            missao.setAgencia(null);
        }

        if (req.organizacaoId() != null) {
            Organizacao org = organizacaoRepository.findById(req.organizacaoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Organização não encontrada: " + req.organizacaoId()));
            missao.setOrganizacao(org);
        } else {
            missao.setOrganizacao(null);
        }
    }

    private MissaoResponse toResponse(Missao m) {
        Double lat = m.getCoordenadas() != null ? m.getCoordenadas().getLatitude() : null;
        Double lng = m.getCoordenadas() != null ? m.getCoordenadas().getLongitude() : null;
        String agenciaId = m.getAgencia() != null ? m.getAgencia().getId() : null;
        String agenciaNome = m.getAgencia() != null ? m.getAgencia().getNome() : null;
        String orgId = m.getOrganizacao() != null ? m.getOrganizacao().getId() : null;
        String orgNome = m.getOrganizacao() != null ? m.getOrganizacao().getNome() : null;

        return new MissaoResponse(
                m.getId(), m.getCodigo(), m.getNome(), m.getDescricao(),
                m.getStatus() != null ? m.getStatus().name() : null,
                m.getValorMeta(), m.getDataLimite(),
                m.getTipoOrbita(), m.getVidaUtil(), m.getCargaUtil(), m.getBadge(),
                lat, lng, agenciaId, agenciaNome, orgId, orgNome
        );
    }
}
