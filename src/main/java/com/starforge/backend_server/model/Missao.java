package com.starforge.backend_server.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_MISSAO")
public class Missao {

    @Id
    @Column(name = "ID_MISSAO")
    private String id;

    @Column(name = "CD_MISSAO")
    private String codigo;

    @Column(name = "NM_MISSAO")
    private String nome;

    @Lob
    @Column(name = "DS_MISSAO")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STT_MISSAO")
    private StatusMissao status;

    @Column(name = "VL_META_MISSAO")
    private BigDecimal valorMeta;

    @Column(name = "DT_LIMITE")
    private LocalDate dataLimite;

    @Column(name = "TP_ORB_MISSAO")
    private String tipoOrbita;

    @Column(name = "VD_UTIL_MISSAO")
    private String vidaUtil;

    @Column(name = "CG_UTIL_MISSAO")
    private String cargaUtil;

    @Column(name = "COORD_LAT_MISSAO")
    private Double latitude;

    @Column(name = "COORD_LNG_MISSAO")
    private Double longitude;

    @Column(name = "BADG_MISSAO")
    private String badge;

    @ManyToOne
    @JoinColumn(name = "TB_AGENCIA_ID_AGENCIA")
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name = "TB_ORGANIZACAO_ID_ORGANIZACAO")
    private Organizacao organizacao;

    @OneToMany(mappedBy = "missao")
    private List<FaseMissao> fases;

    @OneToMany(mappedBy = "missao")
    private List<Contribuicao> contribuicoes;

    @OneToOne(mappedBy = "missao")
    private Nave nave;
}