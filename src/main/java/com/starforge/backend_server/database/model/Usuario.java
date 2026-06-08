package com.starforge.backend_server.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_USUARIO",
        uniqueConstraints = @UniqueConstraint(
                name = "TB_USUARIO_EM_USUARIO_UN",
                columnNames = {"EM_USUARIO"}
        ))
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @Column(name = "ID_USUARIO")
    private String id;

    @Column(name = "RL_USUARIO", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "NM_USUARIO", nullable = false, length = 100)
    private String nome;

    @Column(name = "EM_USUARIO", nullable = false, length = 100)
    private String email;

    @Column(name = "SEN_HASH_USUARIO", nullable = false)
    private String senhaHash;

    @Column(name = "STT_USUARIO", nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private StatusUsuario status;

    @Column(name = "DT_CADST_USUARIO")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) dataCadastro = LocalDate.now();
        if (status == null) status = StatusUsuario.ATIVO;
        if (role == null) role = UserRole.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (UserRole.ADMIN.equals(this.role)) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return status == StatusUsuario.ATIVO;
    }
}
