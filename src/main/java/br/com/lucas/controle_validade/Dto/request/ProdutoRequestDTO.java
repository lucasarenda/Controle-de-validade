package br.com.lucas.controle_validade.Dto.request;

import br.com.lucas.controle_validade.model.Estabelecimento;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
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
                                UUID estabelecimentoId) {

}
