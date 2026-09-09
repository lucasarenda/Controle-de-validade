package br.com.lucas.controle_validade.Dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteUpdateDTO(
        @Pattern(regexp = "(?s).*\\S.*", message = "Número do lote é obrigatório")
        String numeroLote,

        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        @DecimalMin(value = "0.0", message = "Custo unitário não pode ser negativo")
        BigDecimal custoUnitario,

        @PastOrPresent(message = "Data de entrada não pode estar no futuro")
        LocalDate dataEntrada,

        LocalDate dataValidade,
        @Pattern(regexp = "(?s).*\\S.*", message = "Endereço é obrigatório")
        String endereco
) {
}
