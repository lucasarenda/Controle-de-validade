package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(
            @RequestBody @Valid UserRequestDTO userRequestDTO) {

            service.cadastrarUser(userRequestDTO);
            return ResponseEntity.ok("Usuario cadastrado com sucesso!!");

    }

    @GetMapping
    public List<UserResponseDTO> buscaTodosUsers() {
       return service.buscaTodosUsers();
    }

}

