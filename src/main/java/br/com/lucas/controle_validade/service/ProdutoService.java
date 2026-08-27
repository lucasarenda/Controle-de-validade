package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.EstabelecimentoNaoPossuiProdutosException;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {
    private final EstabelecimentoRepository repositoryEstabelecimento;
    private final ProdutoRepository repository;

    public ProdutoService(EstabelecimentoRepository repositoryEstabelecimento, ProdutoRepository repository) {
        this.repositoryEstabelecimento = repositoryEstabelecimento;
        this.repository = repository;
    }

    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(dto.estabelecimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));
        Produto produto = repository.save(new Produto(dto, estabelecimento));
        return new ProdutoResponseDTO(produto);
    }

    public List<ProdutoResponseDTO> buscaProdutosPorEstabelecimento(UUID id) {
        List<Produto> produtos = repository.findByEstabelecimento_Id(id);
        if (produtos.isEmpty()) {
            throw new EstabelecimentoNaoPossuiProdutosException("Estabelecimento não possui produtos cadastrados");
        }
        return produtos.stream().map(ProdutoResponseDTO::new).toList();
    }

    public void removerProduto(UUID id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        repository.delete(produto);
    }
}
