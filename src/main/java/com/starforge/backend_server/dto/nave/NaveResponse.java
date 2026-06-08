package com.starforge.backend_server.dto.nave;

public record NaveResponse(
        String id,
        String nome,
        String classe,
        String imagemUrl,
        String missaoId,
        String missaoNome
) {}
