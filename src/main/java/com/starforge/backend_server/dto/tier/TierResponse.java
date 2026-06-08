package com.starforge.backend_server.dto.tier;

import java.math.BigDecimal;

public record TierResponse(
        String id,
        String nome,
        BigDecimal valorMinimo,
        boolean acessoAntecipado,
        String descricao
) {}
