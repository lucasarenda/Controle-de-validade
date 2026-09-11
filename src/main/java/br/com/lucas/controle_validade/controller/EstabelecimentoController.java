package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.EstabelecimentoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import br.com.lucas.controle_validade.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {
    private final EstabelecimentoService service;
    private final ProdutoService produtoService;

    public EstabelecimentoController(EstabelecimentoService service, ProdutoService produtoService) {
        this.service = service;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> cadastrarEstabelecimento(
            @RequestBody @Valid EstabelecimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarEstabelecimento(dto));
    }

    @GetMapping("/{estabelecimentoId}/produtos")
    public List<ProdutoResponseDTO> buscarProdutos(@PathVariable UUID estabelecimentoId) {
        return produtoService.buscaProdutosPorEstabelecimento(estabelecimentoId);
    }

    @GetMapping("/{id}")
    public EstabelecimentoResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEstabelecimento(@PathVariable UUID id) {
        service.removeEstabelecimento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> atualizarEstabelecimento(
            @PathVariable UUID id,
            @RequestBody @Valid EstabelecimentoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizarEstabelecimento(id, dto));
    }
}
