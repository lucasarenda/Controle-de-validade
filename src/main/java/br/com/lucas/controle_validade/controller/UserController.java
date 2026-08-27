package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UserRequestDTO dto) {
        service.cadastrarUser(dto);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!!");
    }

    @GetMapping
    public List<UserResponseDTO> buscaTodosUsers() {
        return service.buscaTodosUsers();
    }

    @GetMapping("/{nome}")
    public UserResponseDTO buscaUsuarioPeloNome(@PathVariable String nome) {
        return service.buscaUsuarioPeloNome(nome);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> removerUsuario(@PathVariable UUID id) {
        service.removerUser(id);
        return ResponseEntity.ok("Usuário removido com sucesso!!");
    }
}
