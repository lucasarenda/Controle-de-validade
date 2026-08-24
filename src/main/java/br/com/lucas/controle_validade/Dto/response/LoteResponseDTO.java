package br.com.lucas.controle_validade.Dto.response;

import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.StatusValidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoteResponseDTO(
        UUID id, String numeroLote, Integer quantidade, BigDecimal custoUnitario,
        LocalDate dataEntrada, LocalDate dataValidade, String endereco,
        UUID produtoId, long diasParaVencimento, StatusValidade status
) {
    public LoteResponseDTO(Lote lote, long dias, StatusValidade status) {
        this(lote.getId(), lote.getNumeroLote(), lote.getQuantidade(), lote.getCustoUnitario(),
                lote.getDataEntrada(), lote.getDataValidade(), lote.getEndereco(),
                lote.getProduto().getId(), dias, status);
    }
}
