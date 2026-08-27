package br.com.lucas.controle_validade.exception.custom;

public class UsuarioNaoPossuiEstabelecimentoException extends RuntimeException {
    public UsuarioNaoPossuiEstabelecimentoException(String message) {
        super(message);
    }
}
