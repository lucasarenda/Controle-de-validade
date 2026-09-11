package br.com.lucas.controle_validade.exception.handler;

import br.com.lucas.controle_validade.exception.custom.DataLoteInvalidaException;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.exception.response.ResponseError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoJaExisteException.class)
    public ResponseEntity<ResponseError> recursoJaExiste(
            RecursoJaExisteException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ResponseError> recursoNaoEncontrado(
            RecursoNaoEncontradoException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(DataLoteInvalidaException.class)
    public ResponseEntity<ResponseError> regraDeNegocio(
            DataLoteInvalidaException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> tratarValidacao(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return criarResposta(HttpStatus.BAD_REQUEST, "Dados inválidos", request, campos);
    }

    private ResponseEntity<ResponseError> criarResposta(HttpStatus status, String mensagem, HttpServletRequest request, Map<String, String> campos) {
        ResponseError erro = new ResponseError(
                LocalDateTime.now(), status.value(), mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(erro);
    }
}
