package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        service.cadastrarProduto(dto);
        return ResponseEntity.ok("Produto cadastrado com sucesso!!");
    }

    @GetMapping("/{id}")
    public List<ProdutoResponseDTO> buscaProdutoPorEstabelecimento(@PathVariable UUID id) {
        return service.buscaProdutosPorEstabelecimento(id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> removerProduto(@PathVariable UUID id) {
        service.removerProduto(id);
        return ResponseEntity.ok("Produto removido com sucesso!!");
    }
}
