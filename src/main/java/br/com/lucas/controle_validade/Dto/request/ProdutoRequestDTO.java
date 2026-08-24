package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProdutoRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotBlank
        String marca,

        @NotBlank
        String categoria,

        @NotNull
        UUID estabelecimentoId
) {
}
