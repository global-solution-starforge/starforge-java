package com.starforge.backend_server.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TB_TIER")
public class Tier {
    @Id
    @Column(name = "ID_TIER")
    private String id;

    @Column(name = "NM_TIER", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NomeTier nome;

    @Column(name = "VL_MIN_TIER", nullable = false, scale = 10, precision = 2)
    private BigDecimal valorMinimo;

    @Column(name = "FL_ACESSO_ANT_TIER", nullable = false)
    private char flagAcessoAntecipado;

    @Column(name = "DS_TIER", length = 255)
    private String descricao;

    @OneToMany(mappedBy = "tier")
    private List<Contribuicao> contribuicoes;

    public Tier(String id, NomeTier nome, BigDecimal valorMinimo, char flagAcessoAntecipado, String descricao) {
        this.id = id;
        this.nome = nome;
        this.valorMinimo = valorMinimo;
        this.flagAcessoAntecipado = flagAcessoAntecipado;
        this.descricao = descricao;
    }

    public Tier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NomeTier getNome() {
        return nome;
    }

    public void setNome(NomeTier nome) {
        this.nome = nome;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public char getFlagAcessoAntecipado() {
        return flagAcessoAntecipado;
    }

    public void setFlagAcessoAntecipado(char flagAcessoAntecipado) {
        this.flagAcessoAntecipado = flagAcessoAntecipado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
