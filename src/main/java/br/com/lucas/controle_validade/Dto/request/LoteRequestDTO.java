package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoteRequestDTO(
        @NotBlank(message = "Número do lote é obrigatório")
        String numeroLote,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        @NotNull(message = "Custo unitário é obrigatório")
        @DecimalMin(value = "0.0", message = "Custo unitário não pode ser negativo")
        BigDecimal custoUnitario,

        @NotNull(message = "Data de entrada é obrigatória")
        @PastOrPresent(message = "Data de entrada não pode estar no futuro")
        LocalDate dataEntrada,

        @NotNull(message = "Data de validade é obrigatória")
        LocalDate dataValidade,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotNull(message = "Id do produto é obrigatório")
        UUID produtoId
) {
}
