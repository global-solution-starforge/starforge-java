package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierRepository extends JpaRepository<Tier, String> {}
