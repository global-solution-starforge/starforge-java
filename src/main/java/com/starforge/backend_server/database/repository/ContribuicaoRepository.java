package com.starforge.backend_server.database.repository;

import com.starforge.backend_server.database.model.Contribuicao;
import com.starforge.backend_server.database.model.StatusContribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ContribuicaoRepository extends JpaRepository<Contribuicao, String> {

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Contribuicao c WHERE c.usuario.id = :usuarioId AND c.status = :status")
    BigDecimal sumValorByUsuarioIdAndStatus(@Param("usuarioId") String usuarioId, @Param("status") StatusContribuicao status);

    @Query("SELECT COUNT(DISTINCT c.missao.id) FROM Contribuicao c WHERE c.usuario.id = :usuarioId AND c.status = :status")
    long countMissoesDistinctByUsuarioIdAndStatus(@Param("usuarioId") String usuarioId, @Param("status") StatusContribuicao status);
}
