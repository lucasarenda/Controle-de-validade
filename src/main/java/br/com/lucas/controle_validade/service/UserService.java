package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.exception.UsuarioJaExisteException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) { this.repository = repository; }

    public UserResponseDTO cadastrarUser(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email().trim().toLowerCase())) {
            throw new UsuarioJaExisteException("Já existe um usuário cadastrado com este email");
        }
        User user = repository.save(new User(dto));
        return new UserResponseDTO(user);
    }

    public List<UserResponseDTO> buscaTodosUsers() {
        return repository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public void removerUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        repository.delete(user);
    }
}
