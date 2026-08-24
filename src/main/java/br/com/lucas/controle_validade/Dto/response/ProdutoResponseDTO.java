package br.com.lucas.controle_validade.Dto.response;

import br.com.lucas.controle_validade.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProdutoResponseDTO(UUID id,

                                 String nome,

                                 String descricao,

                                 String marca,

                                 String categoria,

                                 LocalDateTime dataCadastro,

                                 UUID estabelecimentoId) {

    public ProdutoResponseDTO(Produto produto){
        this(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getMarca(),
                produto.getCategoria(),
                produto.getDataCadastro(),
                produto.getEstabelecimento().getId()
        );
    }
}
