package com.starforge.backend_server.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TB_ORGANIZACAO")
public class Organizacao {

    @Id
    @Column(name = "ID_ORGANIZACAO")
    private String id;

    @Column(name = "NM_ORGANIZACAO", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "organizacao")
    private List<Missao> missoes;
}