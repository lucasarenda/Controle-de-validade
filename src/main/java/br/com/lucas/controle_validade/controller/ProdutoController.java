package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import br.com.lucas.controle_validade.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO) {

        service.cadastrarProduto(produtoRequestDTO);
        return ResponseEntity.ok("Produto cadastrado com sucesso!!");

    }

    @GetMapping("/{id}")
    public List <ProdutoResponseDTO> buscaProdutoPorEstabelecimento(@PathVariable UUID id){
        return service.buscaProdutosPorEstabelecimento(id);
    }
}