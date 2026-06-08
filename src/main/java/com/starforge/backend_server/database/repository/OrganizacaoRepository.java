package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, String> {}
