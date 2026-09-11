package br.com.lucas.controle_validade.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @NotNull
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Estabelecimento> estabelecimentos = new ArrayList<>();

    protected User() {
    }

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email.trim().toLowerCase();
        this.senha = senha;
        this.dataCadastro = LocalDateTime.now();
    }

    public void alterarNome(String nome) { this.nome = nome; }
    public void alterarEmail(String email) { this.email = email.trim().toLowerCase(); }
}
