package br.com.lucas.controle_validade.exception.handler;

import br.com.lucas.controle_validade.exception.custom.*;
import br.com.lucas.controle_validade.exception.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoJaExisteException.class)
    public ResponseEntity<ResponseError> recursoJaExiste(RecursoJaExisteException exception) {

        ResponseError response = new ResponseError(
                exception.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({UsuarioNaoPossuiEstabelecimentoException.class,
            EstabelecimentoNaoPossuiProdutosException.class,
            ProdutoNaoPossuiLotesException.class,
            RecursoNaoEncontradoException.class})

    public ResponseEntity<ResponseError> recursoNaoEncontrado(RuntimeException exception) {

        ResponseError response = new ResponseError(
                exception.getMessage(),HttpStatus.NOT_FOUND, LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException exception) {
        Map<String, String> erros = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erros);
    }
}
