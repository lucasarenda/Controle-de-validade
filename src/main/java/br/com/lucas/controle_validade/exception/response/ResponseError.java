package br.com.lucas.controle_validade.exception.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(String mensagem,
                            HttpStatus httpStatus,
                            LocalDateTime localDateTime) {
}
