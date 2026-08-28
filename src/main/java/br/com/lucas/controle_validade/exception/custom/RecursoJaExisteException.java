package br.com.lucas.controle_validade.exception.custom;

public class RecursoJaExisteException extends RuntimeException {
    public RecursoJaExisteException(String message) {
        super(message);
    }
}
