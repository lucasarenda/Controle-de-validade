package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProdutoRequestDTO(
        @NotBlank(message = "Nome obrigatório.")
        String nome,

        @NotBlank(message = "Descrição obrigatório.")
        String descricao,

        @NotBlank(message = "Marca obrigatório.")
        String marca,

        @NotBlank(message = "Categoria obrigatório.")
        String categoria,

        @NotNull(message = "Id do estabelecimento obrigatório.")
        UUID estabelecimentoId
) {
}
