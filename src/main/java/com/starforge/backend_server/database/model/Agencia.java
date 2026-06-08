package com.starforge.backend_server.database.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TB_AGENCIA")
public class Agencia {

    @Id
    @Column(name = "ID_AGENCIA")
    private String id;

    @Column(name = "NM_AGENCIA", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "agencia")
    private List<Missao> missoes;
}