package com.starforge.backend_server.dto.missao;

import com.starforge.backend_server.database.model.StatusMissao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MissaoRequest(
        String codigo,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao,

        StatusMissao status,

        @NotNull(message = "Valor meta é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor meta deve ser positivo")
        BigDecimal valorMeta,

        LocalDate dataLimite,
        String tipoOrbita,
        String vidaUtil,
        String cargaUtil,
        String badge,
        Double latitude,
        Double longitude,
        String agenciaId,
        String organizacaoId
) {}
