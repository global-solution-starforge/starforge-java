package com.starforge.backend_server.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TB_CONTRIBUICAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contribuicao {

    @Id
    @Column(name = "ID_CONTRIBUICAO")
    private String id;

    @Column(name = "VL_CONTRIBUICAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STT_CONTRIBUICAO", nullable = false)
    private StatusContribuicao status;

    @Enumerated(EnumType.STRING)
    @Column(name = "MTD_PGT_CONTRIBUICAO", nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(name = "DT_CONTRIBUICAO", nullable = false)
    private LocalDate dataContribuicao;

    @ManyToOne
    @JoinColumn(name = "TB_USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO", nullable = false)
    private Missao missao;

    @ManyToOne
    @JoinColumn(name = "TB_TIER_ID_TIER", nullable = false)
    private Tier tier;

    @OneToOne(mappedBy = "contribuicao")
    private Hangar hangar;
}
