package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Nave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NaveRepository extends JpaRepository<Nave, String> {
    Optional<Nave> findByMissaoId(String missaoId);
}
