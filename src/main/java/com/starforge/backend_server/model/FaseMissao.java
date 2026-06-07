package com.starforge.backend_server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_FASE_MISSAO")
public class FaseMissao {
    @Id
    @Column(name = "ID_FASE_MISSAO")
    private String id;

    @Column(name = "NMR_FASE_MISSAO", nullable = false, scale = 1)
    private int numeroFase;

    @Column(name = "NM_FASE_MISSAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_FASE_MISSAO")
    private String descricao;

    @Column(name = "STT_FASE", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusFaseMissao status;

    @Column(name = "PCT_FASE_MISSAO", nullable = false, precision = 5, scale = 2)
    private float porcentagem;

    @ManyToOne
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO")
    private Missao missao;

    public FaseMissao(String id, int numeroFase, String nome, String descricao, StatusFaseMissao status, float porcentagem, Missao missao) {
        this.id = id;
        this.numeroFase = numeroFase;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.porcentagem = porcentagem;
        this.missao = missao;
    }

    public FaseMissao() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumeroFase() {
        return numeroFase;
    }

    public void setNumeroFase(int numeroFase) {
        this.numeroFase = numeroFase;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusFaseMissao getStatus() {
        return status;
    }

    public void setStatus(StatusFaseMissao status) {
        this.status = status;
    }

    public float getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(float porcentagem) {
        this.porcentagem = porcentagem;
    }

    public Missao getMissao() {
        return missao;
    }

    public void setMissao(Missao missao) {
        this.missao = missao;
    }
}
