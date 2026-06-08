package com.starforge.backend_server.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TB_NAVE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nave {

    @Id
    @Column(name = "ID_NAVE")
    private String id;

    @Column(name = "NM_NAVE", nullable = false)
    private String nome;

    @Column(name = "CLS_NAVE")
    private String classe;

    @Column(name = "IMG_URL_NAVE", length = 255)
    private String imagemUrl;

    @OneToOne
    @JoinColumn(name = "TB_MISSAO_ID_MISSAO", nullable = false, unique = true)
    private Missao missao;

    @OneToMany(mappedBy = "nave")
    private List<Hangar> hangares;
}
