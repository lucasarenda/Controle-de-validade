package br.com.lucas.controle_validade.exception.custom;

public class ProdutoNaoPossuiLotesException extends RuntimeException {
    public ProdutoNaoPossuiLotesException(String message) {
        super(message);
    }
}
