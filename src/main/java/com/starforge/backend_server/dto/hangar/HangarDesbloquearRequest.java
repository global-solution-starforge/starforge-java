package com.starforge.backend_server.dto.hangar;

import jakarta.validation.constraints.NotBlank;

public record HangarDesbloquearRequest(
        @NotBlank(message = "hangarId é obrigatório")
        String hangarId,

        String nomeGravado
) {}
