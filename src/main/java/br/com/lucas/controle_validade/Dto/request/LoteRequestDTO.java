package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.*;

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
        @Pattern(
                regexp = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$",
                message = "A data deve estar no formato dd/MM/yyyy HH:mm"
        )
        LocalDate dataEntrada,

        @NotNull
        @Pattern(
                regexp = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$",
                message = "A data deve estar no formato dd/MM/yyyy HH:mm"
        )
        LocalDate dataValidade,

        @NotBlank
        String endereco,

        @NotNull
        UUID produtoId
) {
}
