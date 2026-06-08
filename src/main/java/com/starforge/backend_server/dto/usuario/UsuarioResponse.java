package com.starforge.backend_server.dto.usuario;

import java.time.LocalDate;

public record UsuarioResponse(
        String id,
        String nome,
        String email,
        String status,
        String role,
        LocalDate dataCadastro
) {}
