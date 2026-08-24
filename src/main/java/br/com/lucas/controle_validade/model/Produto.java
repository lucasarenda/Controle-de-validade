package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.model.Estabelecimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotBlank
    private String marca;

    @NotBlank
    private String categoria;

    @ManyToOne
    @JoinColumn(name  = "estabelecimento_id")
    private Estabelecimento estabelecimento;


    @NotNull
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "produto")
    private List<Lote> lotes = new ArrayList<>();

    public Produto(ProdutoRequestDTO produtoRequestDTO, Estabelecimento estabelecimento) {
        this.nome = produtoRequestDTO.nome();
        this.descricao = produtoRequestDTO.descricao();
        this.marca = produtoRequestDTO.marca();
        this.categoria = produtoRequestDTO.categoria();
        this.estabelecimento = estabelecimento;
        this.dataCadastro = LocalDateTime.now();
    }
}