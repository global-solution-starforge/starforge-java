package com.starforge.backend_server.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TB_NAVE")
public class Nave {

    @Id
    @Column(name = "ID_NAVE")
    private String id;

    @Column(name = "NM_NAVE")
    private String nome;

    @Column(name = "CLS_NAVE")
    private String classe;

    @Column(name = "IMG_URL_NAVE")
    private String imagemUrl;

    @OneToOne
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO")
    private Missao missao;

    @OneToMany(mappedBy = "nave")
    private List<Hangar> hangares;
}