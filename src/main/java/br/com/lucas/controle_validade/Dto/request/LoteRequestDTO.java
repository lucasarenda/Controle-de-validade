package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoteRequestDTO(
        @NotBlank
        String numeroLote,

        @NotNull
        @Positive Integer quantidade,

        @NotNull
        @DecimalMin("0.0") BigDecimal custoUnitario,

        @NotNull
        LocalDate dataEntrada,

        @NotNull
        LocalDate dataValidade,

        @NotBlank
        String endereco,

        @NotNull
        UUID produtoId
) {
}
