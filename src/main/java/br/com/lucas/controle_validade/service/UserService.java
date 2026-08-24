package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.UsuarioJaExisteException;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;


    public UserResponseDTO cadastrarUser(UserRequestDTO userRequestDTO) {
        if (repository.existsByEmail(userRequestDTO.email())) {
            throw new UsuarioJaExisteException(
                    "Já existe um usuário cadastrado com este email"
            );
        }
            User user = repository.save(new User(userRequestDTO));

            return new UserResponseDTO(user);



    }

    public List<UserResponseDTO> buscaTodosUsers() {
        return repository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();

    }
}
