package com.starforge.backend_server.dto.usuario;

import java.math.BigDecimal;

public record UsuarioResumoResponse(
        String usuarioId,
        BigDecimal totalContribuido,
        long missoesApoiadas
) {}
