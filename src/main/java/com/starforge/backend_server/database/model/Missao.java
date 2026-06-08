package com.starforge.backend_server.database.model;

import com.starforge.backend_server.database.model.embedded.Coordenadas;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_MISSAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Missao {

    @Id
    @Column(name = "ID_MISSAO")
    private String id;

    @Column(name = "CD_MISSAO", unique = true)
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

    @Embedded
    private Coordenadas coordenadas;

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
