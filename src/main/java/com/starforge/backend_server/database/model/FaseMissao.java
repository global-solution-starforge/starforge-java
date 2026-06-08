package com.starforge.backend_server.database.model;

import com.starforge.backend_server.database.model.embedded.FaseMissaoId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_FASE_MISSAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FaseMissao {

    @EmbeddedId
    private FaseMissaoId id;

    @Column(name = "NM_FASE_MISSAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_FASE_MISSAO")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STT_FASE", nullable = false, length = 20)
    private StatusFaseMissao status;

    @Column(name = "PCT_FASE_MISSAO", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO")
    private Missao missao;
}
