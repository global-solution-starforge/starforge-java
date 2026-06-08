package com.starforge.backend_server.dto.contribuicao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContribuicaoResponse(
        String id,
        BigDecimal valor,
        String status,
        String metodoPagamento,
        LocalDate dataContribuicao,
        String usuarioId,
        String usuarioNome,
        String missaoId,
        String missaoNome,
        String tierId,
        String tierNome,
        String hangarId
) {}
