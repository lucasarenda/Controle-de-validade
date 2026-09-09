package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.Pattern;

public record ProdutoUpdateDTO(
        @Pattern(regexp = "(?s).*\\S.*", message = "Nome obrigatório.")
        String nome,
        @Pattern(regexp = "(?s).*\\S.*", message = "Descrição obrigatório.")
        String descricao,
        @Pattern(regexp = "(?s).*\\S.*", message = "Marca obrigatório.")
        String marca,
        @Pattern(regexp = "(?s).*\\S.*", message = "Categoria obrigatório.")
        String categoria
) {
}
