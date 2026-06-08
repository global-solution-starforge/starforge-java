package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.FaseMissao;
import com.starforge.backend_server.database.model.embedded.FaseMissaoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaseMissaoRepository extends JpaRepository<FaseMissao, FaseMissaoId> {
    List<FaseMissao> findByIdMissaoIdOrderByIdNumeroFase(String missaoId);
    Optional<FaseMissao> findByIdMissaoIdAndIdNumeroFase(String missaoId, int numeroFase);
}
