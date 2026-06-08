package com.starforge.backend_server.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TB_ORGANIZACAO")
@AttributeOverrides({
        @AttributeOverride(name = "id",   column = @Column(name = "ID_ORGANIZACAO")),
        @AttributeOverride(name = "nome", column = @Column(name = "NM_ORGANIZACAO", nullable = false))
})
@Getter
@Setter
@NoArgsConstructor
public class Organizacao extends Instituicao {

    @OneToMany(mappedBy = "organizacao")
    private List<Missao> missoes;

    public Organizacao(String id, String nome) {
        super(id, nome);
    }

    public Organizacao(String id, String nome, List<Missao> missoes) {
        super(id, nome);
        this.missoes = missoes;
    }
}
