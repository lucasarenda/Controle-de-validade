package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.UserRequestDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.Dto.response.UserResponseDTO;
import br.com.lucas.controle_validade.exception.UsuarioJaExisteException;
import br.com.lucas.controle_validade.exception.UsuarioNaoPossuiEstabelecimentoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {
    @Autowired
    private EstabelecimentoRepository repositoryEstabelecimento;

    @Autowired
    private ProdutoRepository repository;


    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {

        }
        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(dto.estabelecimentoId())
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        Produto produto = new Produto(dto, estabelecimento);

        Produto produtoSalvo =
                repository.save(produto);

        return new ProdutoResponseDTO(produto);


    }
    public List<ProdutoResponseDTO> buscaProdutosPorEstabelecimento(UUID id) {

        List<Produto> produtos =
                repository.findByEstabelecimento_Id(id);

        if (produtos.isEmpty()) {
            throw new UsuarioNaoPossuiEstabelecimentoException("Estabelecimento não possui produtos cadastrados");
        }
        return produtos.stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }
}