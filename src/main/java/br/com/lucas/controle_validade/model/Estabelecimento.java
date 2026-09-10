package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.EstabelecimentoUpdateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cnpj;

    private String telefone;

    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "estabelecimento",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    public Estabelecimento(String nome, String email, String cnpj, String telefone, String endereco, User user) {
        this.nome = nome;
        this.email = email.trim().toLowerCase();
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.user = user;
    }
}
