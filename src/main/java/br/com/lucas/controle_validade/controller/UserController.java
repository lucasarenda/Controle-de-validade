package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import br.com.lucas.controle_validade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    private final UserService service;
    private final EstabelecimentoService estabelecimentoService;

    public UserController(UserService service, EstabelecimentoService estabelecimentoService) {
        this.service = service;
        this.estabelecimentoService = estabelecimentoService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> cadastrarUsuario(@RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarUser(dto));
    }

    @GetMapping
    public List<UserResponseDTO> buscarUsuarios(@RequestParam(required = false) String nome) {
        return nome == null ? service.buscaTodosUsers() : List.of(service.buscaUsuarioPeloNome(nome));
    }

    @GetMapping("/{id}")
    public UserResponseDTO buscarUsuarioPorId(@PathVariable UUID id) {
        return service.buscaUsuarioPorId(id);
    }

    @GetMapping("/{usuarioId}/estabelecimentos")
    public List<EstabelecimentoResponseDTO> buscarEstabelecimentos(@PathVariable UUID usuarioId) {
        return estabelecimentoService.buscaEstabelecimentoPorUsuario(usuarioId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable UUID id) {
        service.removerUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> atualizarUsuario(
            @PathVariable UUID id,
            @RequestBody @Valid UserUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizarUser(id, dto));
    }
}
