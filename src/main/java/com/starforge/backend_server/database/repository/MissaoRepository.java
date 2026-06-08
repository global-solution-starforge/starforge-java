package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Missao;
import com.starforge.backend_server.database.model.StatusMissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissaoRepository extends JpaRepository<Missao, String> {
    List<Missao> findByStatus(StatusMissao status);
}
