package br.com.lucas.controle_validade.Dto.response;

import br.com.lucas.controle_validade.model.Estabelecimento;

import java.util.UUID;

public record EstabelecimentoResponseDTO(

        UUID id,
        String nome,
        String email,
        String cnpj,
        String telefone,
        String endereco,
        UUID usuarioId

) {

    public EstabelecimentoResponseDTO(Estabelecimento estabelecimento) {
        this(
                estabelecimento.getId(),
                estabelecimento.getNome(),
                estabelecimento.getEmail(),
                estabelecimento.getCnpj(),
                estabelecimento.getTelefone(),
                estabelecimento.getEndereco(),
                estabelecimento.getUser().getId()
        );
    }
}