package com.starforge.backend_server.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TB_TIER")
@Getter
@Setter
@NoArgsConstructor
public class Tier {

    @Id
    @Column(name = "ID_TIER")
    private String id;

    @Column(name = "NM_TIER", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NomeTier nome;

    @Column(name = "VL_MIN_TIER", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMinimo;

    @Column(name = "FL_ACESSO_ANT_TIER", nullable = false)
    private char flagAcessoAntecipado;

    @Column(name = "DS_TIER", length = 255)
    private String descricao;

    @OneToMany(mappedBy = "tier")
    private List<Contribuicao> contribuicoes;
}
