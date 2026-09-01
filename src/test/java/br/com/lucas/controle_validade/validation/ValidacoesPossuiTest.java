package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.EstabelecimentoNaoPossuiProdutosException;
import br.com.lucas.controle_validade.exception.custom.ProdutoNaoPossuiLotesException;
import br.com.lucas.controle_validade.exception.custom.UsuarioNaoPossuiEstabelecimentoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacoesPossuiTest {

    @Test
    void deveImpedirUsuarioSemEstabelecimentos() {
        var validacao = new ValidacaoUsuarioPossuiEstabelecimentos();

        var exception = assertThrows(
                UsuarioNaoPossuiEstabelecimentoException.class,
                () -> validacao.validar(List.of())
        );

        assertEquals("Usuário não possui estabelecimentos cadastrados", exception.getMessage());
    }

    @Test
    void devePermitirUsuarioComEstabelecimento() {
        var validacao = new ValidacaoUsuarioPossuiEstabelecimentos();

        assertDoesNotThrow(() -> validacao.validar(List.of(new Estabelecimento())));
    }

    @Test
    void deveImpedirEstabelecimentoSemProdutos() {
        var validacao = new ValidacaoEstabelecimentoPossuiProdutos();

        var exception = assertThrows(
                EstabelecimentoNaoPossuiProdutosException.class,
                () -> validacao.validar(List.of())
        );

        assertEquals("Estabelecimento não possui produtos cadastrados", exception.getMessage());
    }

    @Test
    void devePermitirEstabelecimentoComProduto() {
        var validacao = new ValidacaoEstabelecimentoPossuiProdutos();

        assertDoesNotThrow(() -> validacao.validar(List.of(new Produto())));
    }

    @Test
    void deveImpedirProdutoSemLotes() {
        var validacao = new ValidacaoProdutoPossuiLotes();

        var exception = assertThrows(
                ProdutoNaoPossuiLotesException.class,
                () -> validacao.validar(List.of())
        );

        assertEquals("Produto não possui lotes cadastrados", exception.getMessage());
    }

    @Test
    void devePermitirProdutoComLote() {
        var validacao = new ValidacaoProdutoPossuiLotes();

        assertDoesNotThrow(() -> validacao.validar(List.of(new Lote())));
    }
}
