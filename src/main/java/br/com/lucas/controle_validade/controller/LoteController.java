package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.request.LoteUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.service.LoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lotes")
public class LoteController {
    private final LoteService service;

    public LoteController(LoteService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<LoteResponseDTO> cadastrarLote(@RequestBody @Valid LoteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarLote(dto));
    }

    @GetMapping("/{id}")
    public LoteResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscaLotePorId(id);
    }
    @GetMapping("/produto/{ProdutoId}")
    public List<LoteResponseDTO> buscarPorLotesPorProduto(@PathVariable UUID ProdutoId) {
        return service.buscaLotesPorProduto(ProdutoId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLote(@PathVariable UUID id) {
        service.removerLote(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> atualizarLote(@PathVariable UUID id, @RequestBody @Valid LoteUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizarLote(id, dto));
    }
}
