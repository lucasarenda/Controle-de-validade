package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Estabelecimento> Estabelecimentos = new ArrayList<>();

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email.trim().toLowerCase();
        this.senha = senha;
        dataCadastro = LocalDateTime.now();
    }
}
