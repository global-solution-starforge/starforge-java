package com.starforge.backend_server.dto.tier;

import com.starforge.backend_server.database.model.NomeTier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TierRequest(
        @NotNull(message = "nome é obrigatório")
        NomeTier nome,

        @NotNull(message = "valorMinimo é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor mínimo deve ser positivo")
        BigDecimal valorMinimo,

        @NotNull(message = "acessoAntecipado é obrigatório")
        Boolean acessoAntecipado,

        String descricao
) {}
