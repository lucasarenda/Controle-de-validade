package br.com.lucas.controle_validade.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "estabelecimentos")
@EqualsAndHashCode(of = "id")
@Getter
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String email;

    @Column(unique = true)
    private String cnpj;

    private String telefone;
    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    protected Estabelecimento() {
    }

    public Estabelecimento(String nome, String email, String cnpj, String telefone,
                           String endereco, User user) {
        this.nome = nome;
        this.email = email.trim().toLowerCase();
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.user = user;
    }

    public void alterarNome(String nome) { this.nome = nome; }
    public void alterarEmail(String email) { this.email = email.trim().toLowerCase(); }
    public void alterarCnpj(String cnpj) { this.cnpj = cnpj; }
    public void alterarTelefone(String telefone) { this.telefone = telefone; }
    public void alterarEndereco(String endereco) { this.endereco = endereco; }
}
