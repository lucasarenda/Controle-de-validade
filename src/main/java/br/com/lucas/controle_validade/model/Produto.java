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
@Table(name = "produtos")
@EqualsAndHashCode(of = "id")
@Getter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String descricao;
    private String marca;
    private String categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private Estabelecimento estabelecimento;

    @NotNull
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lote> lotes = new ArrayList<>();

    protected Produto() {
    }

    public Produto(String nome, String descricao, String marca, String categoria,
                   Estabelecimento estabelecimento) {
        this.nome = nome;
        this.descricao = descricao;
        this.marca = marca;
        this.categoria = categoria;
        this.estabelecimento = estabelecimento;
        this.dataCadastro = LocalDateTime.now();
    }

    public void alterarNome(String nome) { this.nome = nome; }
    public void alterarDescricao(String descricao) { this.descricao = descricao; }
    public void alterarMarca(String marca) { this.marca = marca; }
    public void alterarCategoria(String categoria) { this.categoria = categoria; }
}
