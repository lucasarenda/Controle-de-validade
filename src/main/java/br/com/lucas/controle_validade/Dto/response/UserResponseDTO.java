package br.com.lucas.controle_validade.Dto.response;

import br.com.lucas.controle_validade.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String email,
        LocalDateTime dataCadastro
) {

    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getDataCadastro()
        );
    }
}