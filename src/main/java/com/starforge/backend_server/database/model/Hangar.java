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

import java.time.LocalDate;

@Entity
@Table(name = "TB_HANGAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hangar {

    @Id
    @Column(name = "ID_HANGAR")
    private String id;

    @Column(name = "DT_DESBLQ_NAVE_HANGAR")
    private LocalDate dataDesbloqueio;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_NAVE_HANGAR", nullable = false)
    private StatusHangar status;

    @Column(name = "NM_GRAVADO_HANGAR")
    private String nomeGravado;

    @ManyToOne
    @JoinColumn(name = "TB_NAVE_ID_NAVE", nullable = false)
    private Nave nave;

    @OneToOne
    @JoinColumn(name = "TB_CONTRIB_ID_CONTRIB", nullable = false, unique = true)
    private Contribuicao contribuicao;
}
