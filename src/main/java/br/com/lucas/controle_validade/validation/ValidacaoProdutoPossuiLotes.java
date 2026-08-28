package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.ProdutoNaoPossuiLotesException;
import br.com.lucas.controle_validade.model.Lote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoProdutoPossuiLotes implements Validacao<List<Lote>> {

    @Override
    public void validar(List<Lote> lotes) {
        if (lotes.isEmpty()) {
            throw new ProdutoNaoPossuiLotesException("Produto não possui lotes cadastrados");
        }
    }
}
