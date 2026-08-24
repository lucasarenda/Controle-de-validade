package br.com.lucas.controle_validade.Dto.request;

import br.com.lucas.controle_validade.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(    @NotBlank(message = "Nome é obrigatório")
                                 @Size(min = 2, max = 100)
                                 String nome,

                                 @NotBlank(message = "Email é obrigatório")
                                 @Email(message = "Email inválido")
                                 String email,

                                 @NotBlank(message = "Senha é obrigatória")
                                 @Size(min = 6, message = "A senha deve possuir pelo menos 6 caracteres")
                                 String senha) {
}
