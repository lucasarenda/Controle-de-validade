package br.com.lucas.controle_validade.exception;

public class ProdutoNaoPossuiLotesException extends RuntimeException {
    public ProdutoNaoPossuiLotesException(String message) {
        super(message);
    }
}
