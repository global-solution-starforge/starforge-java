package com.starforge.backend_server.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TB_CONTRIBUICAO")
public class Contribuicao {

    @Id
    @Column(name = "ID_CONTRIBUICAO")
    private String id;

    @Column(name = "VL_CONTRIBUICAO")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STT_CONTRIBUICAO")
    private StatusContribuicao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "MTD_PGT_CONTRIBUICAO")
    private MetodoPagamento metodoPagamento;

    @Column(name = "DT_CONTRIBUICAO")
    private LocalDate dataContribuicao;

    @ManyToOne
    @JoinColumn(name = "TB_USUARIO_ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO")
    private Missao missao;

    @ManyToOne
    @JoinColumn(name = "TB_TIER_ID_TIER")
    private Tier tier;

    @OneToOne(mappedBy = "contribuicao")
    private Hangar hangar;
}