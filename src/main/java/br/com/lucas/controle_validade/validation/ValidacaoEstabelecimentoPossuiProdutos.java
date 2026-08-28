package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.EstabelecimentoNaoPossuiProdutosException;
import br.com.lucas.controle_validade.model.Produto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoEstabelecimentoPossuiProdutos implements Validacao<List<Produto>> {

    @Override
    public void validar(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            throw new EstabelecimentoNaoPossuiProdutosException("Estabelecimento não possui produtos cadastrados");
        }
    }
}
