package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEmailUsuarioUnico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserResponseDTO cadastrarUser(UserRequestDTO dto) {
        String email = normalizarEmail(dto.email());
        validacaoEmailUsuarioUnico.validar(email);
        return new UserResponseDTO(repository.save(new User(dto.nome().trim(), email, dto.senha())));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> buscaTodosUsers() {
        return repository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO buscaUsuarioPorId(UUID id) {
        return new UserResponseDTO(buscarUser(id));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO buscaUsuarioPeloNome(String nome) {
        User user = repository.findByNomeIgnoreCase(nome.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }

    @Transactional
    public void removerUser(UUID id) {
        repository.delete(buscarUser(id));
    }

    @Transactional
    public UserResponseDTO atualizarUser(UUID id, UserUpdateDTO dto) {
        User user = buscarUser(id);
        if (dto.nome() != null) {
            user.alterarNome(dto.nome().trim());
        }
        if (dto.email() != null) {
            String email = normalizarEmail(dto.email());
            if (!email.equalsIgnoreCase(user.getEmail())) {
                validacaoEmailUsuarioUnico.validar(email);
                user.alterarEmail(email);
            }
        }
        return new UserResponseDTO(user);
    }

    private User buscarUser(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }
}
