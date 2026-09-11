package br.com.lucas.controle_validade.exception.custom;

public class DataLoteInvalidaException extends RuntimeException {
    public DataLoteInvalidaException(String message) {
        super(message);
    }
}
