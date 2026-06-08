package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenciaRepository extends JpaRepository<Agencia, String> {}
