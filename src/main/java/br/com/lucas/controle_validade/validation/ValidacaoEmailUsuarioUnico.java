package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.exception.custom.UsuarioJaExisteException;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEmailUsuarioUnico implements Validacao<UserRequestDTO> {
    private final UserRepository repository;

    public ValidacaoEmailUsuarioUnico(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email().trim().toLowerCase())) {
            throw new UsuarioJaExisteException("Já existe um usuário cadastrado com este email");
        }
    }
}
