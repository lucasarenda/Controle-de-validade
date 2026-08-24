package br.com.lucas.controle_validade.controller;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.service.EstabelecimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/estabelecimentos")
public class EstabelecimentoController {
   @Autowired
    private EstabelecimentoService service;

   @PostMapping
   @Transactional
   public ResponseEntity<String> cadastrarEstabelecimento(@RequestBody @Valid EstabelecimentoRequestDTO estabelecimentoRequestDTO) {

       service.cadastrarEstabelecimento(estabelecimentoRequestDTO);
       return ResponseEntity.ok("Estabelecimento cadastrado com sucesso!!");

   }
   @GetMapping("/{id}")
   public List<EstabelecimentoResponseDTO> buscaEstabelecimentoPorUsuario(@PathVariable UUID id) {
       return service.buscaEstabelecimentoPorUsuario(id);
   }

   @DeleteMapping("/{id}")
   @Transactional
   public  ResponseEntity<String> removerEstabelecimento(@PathVariable UUID id){
       service.removeEstabelecimento(id);
       return ResponseEntity.ok("Estabelecimento removido com sucesso !!");
   }


}
