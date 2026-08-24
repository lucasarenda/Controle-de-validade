package br.com.lucas.controle_validade.Dto.request;

import br.com.lucas.controle_validade.model.Estabelecimento;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record EstabelecimentoRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String cnpj,

        @NotBlank
        String telefone,

        @NotBlank
        String endereco,

        @NotNull
        UUID usuarioId

) {
}