package com.starforge.backend_server.dto.contribuicao;

import com.starforge.backend_server.database.model.MetodoPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContribuicaoRequest(
        @NotBlank(message = "missaoId é obrigatório")
        String missaoId,

        @NotBlank(message = "tierId é obrigatório")
        String tierId,

        @NotNull(message = "valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "metodoPagamento é obrigatório")
        MetodoPagamento metodoPagamento
) {}
