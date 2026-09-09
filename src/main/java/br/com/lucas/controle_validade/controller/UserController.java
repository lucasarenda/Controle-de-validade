package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping("/users/cadastrar")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UserRequestDTO dto) {
        service.cadastrarUser(dto);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!!");
    }

    @GetMapping("/users")
    public List<UserResponseDTO> buscaTodosUsers() {
        return service.buscaTodosUsers();
    }

    @GetMapping("/users/{nome}")
    public UserResponseDTO buscaUsuarioPeloNome(@PathVariable String nome) {
        return service.buscaUsuarioPeloNome(nome);
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public ResponseEntity<String> removerUsuario(@PathVariable UUID id) {
        service.removerUser(id);
        return ResponseEntity.ok("Usuário removido com sucesso!!");
    }

    @PatchMapping("/usuarios/{id}")
    @Transactional
    public ResponseEntity<UserResponseDTO> atualizarUsuario(
            @PathVariable UUID id,
            @RequestBody @Valid UserUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.atualizarUser(id, dto));
    }
}
