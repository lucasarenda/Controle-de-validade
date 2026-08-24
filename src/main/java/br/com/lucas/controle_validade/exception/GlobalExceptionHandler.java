package br.com.lucas.controle_validade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<String> handleEmailJaCadastrado(
            UsuarioJaExisteException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoPossuiEstabelecimentoException.class)
    public ResponseEntity<String> UsuarioNaoPossuiEstabelecimento(
            UsuarioNaoPossuiEstabelecimentoException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(EstabelecimentoNaoPossuiProdutosException.class)
    public ResponseEntity<String> EstabelecimentoNaoPossuiProdutos(
            UsuarioJaExisteException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(erros);
    }
}
