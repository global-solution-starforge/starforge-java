package com.starforge.backend_server.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TB_AGENCIA")
@AttributeOverrides({
        @AttributeOverride(name = "id",   column = @Column(name = "ID_AGENCIA")),
        @AttributeOverride(name = "nome", column = @Column(name = "NM_AGENCIA", nullable = false, unique = true))
})
@Getter
@Setter
@NoArgsConstructor
public class Agencia extends Instituicao {

    @OneToMany(mappedBy = "agencia")
    private List<Missao> missoes;

    public Agencia(String id, String nome) {
        super(id, nome);
    }

    public Agencia(String id, String nome, List<Missao> missoes) {
        super(id, nome);
        this.missoes = missoes;
    }
}
