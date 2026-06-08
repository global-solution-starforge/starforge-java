package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Hangar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HangarRepository extends JpaRepository<Hangar, String> {
    List<Hangar> findByContribuicao_UsuarioId(String usuarioId);
}
