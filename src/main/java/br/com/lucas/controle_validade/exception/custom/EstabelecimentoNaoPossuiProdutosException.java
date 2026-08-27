package br.com.lucas.controle_validade.exception.custom;

public class EstabelecimentoNaoPossuiProdutosException extends RuntimeException {
    public EstabelecimentoNaoPossuiProdutosException(String message) {
        super(message);
    }
}
