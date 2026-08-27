package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
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
@Table(name = "users")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;


    @Column(unique = true)
    private String nome;


    @Column(unique = true)
    private String email;


    private String senha;

    @NotNull
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Estabelecimento> Estabelecimentos = new ArrayList<>();

    public User(UserRequestDTO userRequestDTO) {
        this.nome = userRequestDTO.nome();
        this.email = userRequestDTO.email().trim().toLowerCase();
        this.senha = userRequestDTO.senha();
        this.dataCadastro = LocalDateTime.now();
    }


}
