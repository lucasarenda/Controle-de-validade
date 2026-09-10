package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEmailUsuarioUnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private  UserRepository repository;

    @Autowired
    private  ValidacaoEmailUsuarioUnico validacaoEmailUsuarioUnico;


    public UserResponseDTO cadastrarUser(UserRequestDTO dto) {
        validacaoEmailUsuarioUnico.validar(dto);
        User user = new User(
                dto.nome() ,
                dto.email().trim().toLowerCase(),
                dto.senha()
                );
        repository.save(user);
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

    public UserResponseDTO atualizarUser(UUID id, UserUpdateDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (dto.nome() != null) {
            user.setNome(dto.nome());
        }

        if (dto.email() != null) {
            String emailNormalizado = dto.email().trim().toLowerCase();

            validacaoEmailUsuarioUnico.validar(emailNormalizado);

            user.setEmail(emailNormalizado);
        }

        repository.save(user);

        return new UserResponseDTO(user);
    }
}
