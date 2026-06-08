package com.starforge.backend_server.database.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_HANGAR")
public class Hangar {

    @Id
    @Column(name = "ID_HANGAR")
    private String id;

    @Column(name = "DT_DESBLQ_NAVE_HANGAR")
    private LocalDate dataDesbloqueio;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_NAVE_HANGAR")
    private StatusHangar status;

    @Column(name = "NM_GRAVADO_HANGAR")
    private String nomeGravado;

    @ManyToOne
    @JoinColumn(name = "TB_NAVE_ID_NAVE")
    private Nave nave;

    @OneToOne
    @JoinColumn(name = "TB_CONTRIB_ID_CONTRIB")
    private Contribuicao contribuicao;
}