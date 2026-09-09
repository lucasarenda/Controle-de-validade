package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record EstabelecimentoUpdateDTO(
        @Pattern(regexp = "(?s).*\\S.*", message = "Nome é obrigatório")
        String nome,

        @Pattern(regexp = "(?s).*\\S.*", message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Pattern(regexp = "(?s).*\\S.*", message = "CNPJ é obrigatório")
        String cnpj,
        @Pattern(regexp = "(?s).*\\S.*", message = "Telefone é obrigatório")
        String telefone,
        @Pattern(regexp = "(?s).*\\S.*", message = "Endereço é obrigatório")
        String endereco
) {
}
