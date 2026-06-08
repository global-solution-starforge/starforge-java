package com.starforge.backend_server.dto.missao;

import com.starforge.backend_server.database.model.StatusFaseMissao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FaseMissaoAtualizacaoRequest(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "status é obrigatório")
        StatusFaseMissao status,

        @NotNull(message = "porcentagem é obrigatória")
        @DecimalMin(value = "0.00", message = "Porcentagem não pode ser negativa")
        @DecimalMax(value = "100.00", message = "Porcentagem não pode ultrapassar 100")
        BigDecimal porcentagem
) {}
