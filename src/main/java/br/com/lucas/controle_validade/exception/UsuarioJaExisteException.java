package br.com.lucas.controle_validade.exception;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException(String message) {
        super(message);
    }
}
