package com.starforge.backend_server.dto.contribuicao;

import com.starforge.backend_server.database.model.StatusContribuicao;
import jakarta.validation.constraints.NotNull;

public record ContribuicaoStatusRequest(
        @NotNull(message = "status é obrigatório")
        StatusContribuicao status
) {}
