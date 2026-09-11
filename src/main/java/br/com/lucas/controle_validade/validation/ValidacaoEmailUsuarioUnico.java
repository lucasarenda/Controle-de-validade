package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.UsuarioJaExisteException;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEmailUsuarioUnico {
    private final UserRepository repository;

    public ValidacaoEmailUsuarioUnico(UserRepository repository) {
        this.repository = repository;
    }

    public void validar(String email) {
        if (repository.existsByEmailIgnoreCase(email.trim())) {
            throw new UsuarioJaExisteException("Já existe um usuário cadastrado com este email");
        }
    }
}
