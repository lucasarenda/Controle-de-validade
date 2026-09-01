package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.service.LoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lotes")
public class LoteController {
    private final LoteService service;

    public LoteController(LoteService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<LoteResponseDTO> cadastrarLote(@RequestBody @Valid LoteRequestDTO dto) {
        return ResponseEntity.ok(service.cadastrarLote(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> buscaLotePorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscaLotePorId(id));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<LoteResponseDTO>> buscaLotesPorProduto(@PathVariable UUID produtoId) {
        return ResponseEntity.ok(service.buscaLotesPorProduto(produtoId));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> removerLote(@PathVariable UUID id) {
        service.removerLote(id);
        return ResponseEntity.ok("Lote removido com sucesso!!");
    }
}
