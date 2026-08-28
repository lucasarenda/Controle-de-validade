package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID id;

    private String nome;


    private String descricao;


    private String marca;


    private String categoria;

    @ManyToOne
    private Estabelecimento estabelecimento;


    @NotNull
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "produto",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
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
