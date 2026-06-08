package com.starforge.backend_server.dto.missao;

import java.math.BigDecimal;

public record FaseMissaoResponse(
        int numeroFase,
        String nome,
        String descricao,
        String status,
        BigDecimal porcentagem
) {}
