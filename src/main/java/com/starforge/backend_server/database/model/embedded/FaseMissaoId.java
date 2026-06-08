package com.starforge.backend_server.database.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FaseMissaoId implements Serializable {

    @Column(name = "TB_MISSAO_ID_MISSAO")
    private String missaoId;

    @Column(name = "NMR_FASE_MISSAO")
    private int numeroFase;
}
