package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.service.LoteService;
import br.com.lucas.controle_validade.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;
    private final LoteService loteService;

    public ProdutoController(ProdutoService service, LoteService loteService) {
        this.service = service;
        this.loteService = loteService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarProduto(dto));
    }

    @GetMapping("/{produtoId}/lotes")
    public List<LoteResponseDTO> buscarLotes(@PathVariable UUID produtoId) {
        return loteService.buscaLotesPorProduto(produtoId);
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable UUID id) { return service.buscarPorId(id); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable UUID id) {
        service.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable UUID id,
            @RequestBody @Valid ProdutoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizarProduto(id, dto));
    }
}
