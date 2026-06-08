package com.starforge.backend_server.dto.nave;

import jakarta.validation.constraints.NotBlank;

public record NaveRequest(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        String classe,
        String imagemUrl,

        @NotBlank(message = "missaoId é obrigatório")
        String missaoId
) {}
