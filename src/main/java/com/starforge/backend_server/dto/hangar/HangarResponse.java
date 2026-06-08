package com.starforge.backend_server.dto.hangar;

import java.time.LocalDate;

public record HangarResponse(
        String id,
        String status,
        LocalDate dataDesbloqueio,
        String nomeGravado,
        String naveId,
        String naveNome,
        String contribuicaoId,
        String missaoNome
) {}
