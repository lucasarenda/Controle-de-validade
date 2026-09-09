package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Pattern(regexp = "(?s).*\\S.*", message = "Nome é obrigatório")
        @Size(min = 2, max = 100)
        String nome,

        @Pattern(regexp = "(?s).*\\S.*", message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email
) {
}
