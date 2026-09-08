package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record EstabelecimentoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotNull(message = "Id do usuário é obrigatório")
        UUID usuarioId

) {
}
