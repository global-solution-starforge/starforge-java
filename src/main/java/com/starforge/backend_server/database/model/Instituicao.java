package com.starforge.backend_server.database.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Superclasse mapeada para entidades que representam instituições parceiras de missões.
 * Subclasses herdam id e nome, sobrescrevendo os nomes de coluna via @AttributeOverride.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Instituicao {

    @Id
    private String id;

    private String nome;
}
