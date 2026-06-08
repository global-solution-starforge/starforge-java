package com.starforge.backend_server.dto.instituicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstituicaoRequest(
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 100)
        String nome
) {}
