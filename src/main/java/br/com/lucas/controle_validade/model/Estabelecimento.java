package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String cnpj;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "estabelecimento",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();

    public Estabelecimento(EstabelecimentoRequestDTO estabelecimentoRequestDTO,User usuario) {
        this.nome = estabelecimentoRequestDTO.nome();
        this.email = estabelecimentoRequestDTO.email();
        this.cnpj = estabelecimentoRequestDTO.cnpj();
        this.telefone = estabelecimentoRequestDTO.telefone();
        this.endereco = estabelecimentoRequestDTO.endereco();
        this.user =  usuario;
    }
}
