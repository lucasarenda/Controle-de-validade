package br.com.lucas.controle_validade.exception.custom;

public class UsuarioJaExisteException extends RecursoJaExisteException {
    public UsuarioJaExisteException(String message) {
        super(message);
    }
}
