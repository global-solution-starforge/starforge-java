package com.starforge.backend_server.dto.missao;

import java.math.BigDecimal;

public record MissaoProgressoResponse(
        String missaoId,
        String nome,
        BigDecimal valorMeta,
        BigDecimal valorArrecadado,
        BigDecimal percentualAlcancado
) {}
