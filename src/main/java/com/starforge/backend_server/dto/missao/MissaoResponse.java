package com.starforge.backend_server.dto.missao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MissaoResponse(
        String id,
        String codigo,
        String nome,
        String descricao,
        String status,
        BigDecimal valorMeta,
        LocalDate dataLimite,
        String tipoOrbita,
        String vidaUtil,
        String cargaUtil,
        String badge,
        Double latitude,
        Double longitude,
        String agenciaId,
        String agenciaNome,
        String organizacaoId,
        String organizacaoNome
) {}
