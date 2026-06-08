package com.starforge.backend_server.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_FASE_MISSAO")
@Getter
@Setter
@NoArgsConstructor
public class FaseMissao {

    @Id
    @Column(name = "ID_FASE_MISSAO")
    private String id;

    @Column(name = "NMR_FASE_MISSAO", nullable = false)
    private int numeroFase;

    @Column(name = "NM_FASE_MISSAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_FASE_MISSAO")
    private String descricao;

    @Column(name = "STT_FASE", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusFaseMissao status;

    @Column(name = "PCT_FASE_MISSAO", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO")
    private Missao missao;
}
