package br.com.lucas.controle_validade.exception.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ResponseError(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        String path,
        Map<String, String> campos
) {
    public ResponseError(LocalDateTime timestamp, int status, String mensagem, String path) {
        this(timestamp, status, mensagem, path, Map.of());
    }
}
