package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEstabelecimentoPossuiProdutos;
import br.com.lucas.controle_validade.validation.ValidacaoNomeProdutoUnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {
    @Autowired
    private  EstabelecimentoRepository repositoryEstabelecimento;

    @Autowired
    private  ProdutoRepository repository;

    @Autowired
    private  ValidacaoEstabelecimentoPossuiProdutos validacaoEstabelecimentoPossuiProdutos;

    @Autowired
    private  ValidacaoNomeProdutoUnico validacaoNomeProdutoUnico;

    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(dto.estabelecimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));
        validacaoNomeProdutoUnico.validar(dto.nome());
        Produto produto = repository.save(new Produto(dto, estabelecimento));
        return new ProdutoResponseDTO(produto);
    }

    public List<ProdutoResponseDTO> buscaProdutosPorEstabelecimento(UUID id) {
        List<Produto> produtos = repository.findByEstabelecimento_Id(id);
        validacaoEstabelecimentoPossuiProdutos.validar(produtos);
        return produtos.stream().map(ProdutoResponseDTO::new).toList();
    }

    public void removerProduto(UUID id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        repository.delete(produto);
    }

    public ProdutoResponseDTO atualizarProduto(UUID id, ProdutoUpdateDTO dto) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if (dto.nome() != null && !dto.nome().equals(produto.getNome())) {
            validacaoNomeProdutoUnico.validar(dto.nome());
        }

        produto.atualizar(dto);
        return new ProdutoResponseDTO(repository.save(produto));
    }
}
