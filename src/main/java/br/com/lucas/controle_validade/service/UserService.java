package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEmailUsuarioUnico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final ValidacaoEmailUsuarioUnico validacaoEmailUsuarioUnico;

    public UserService(UserRepository repository, ValidacaoEmailUsuarioUnico validacaoEmailUsuarioUnico) {
        this.repository = repository;
        this.validacaoEmailUsuarioUnico = validacaoEmailUsuarioUnico;
    }

    public UserResponseDTO cadastrarUser(UserRequestDTO dto) {
        validacaoEmailUsuarioUnico.validar(dto);
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

    public UserResponseDTO buscaUsuarioPeloNome(String nome) {
        return repository.findByNome(nome);
    }
}
