package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.ProdutoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoNomeProdutoUnico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ProdutoRepository repository;
    private final ValidacaoNomeProdutoUnico validacaoNome;

    public ProdutoService(EstabelecimentoRepository estabelecimentoRepository,
                          ProdutoRepository repository, ValidacaoNomeProdutoUnico validacaoNome) {
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.repository = repository;
        this.validacaoNome = validacaoNome;
    }

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(dto.estabelecimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));
        String nome = normalizarNome(dto.nome());
        validacaoNome.validar(nome, dto.estabelecimentoId());
        Produto produto = new Produto(nome, dto.descricao().trim(), dto.marca().trim(),
                dto.categoria().trim(), estabelecimento);
        return new ProdutoResponseDTO(repository.save(produto));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscaProdutosPorEstabelecimento(UUID estabelecimentoId) {
        if (!estabelecimentoRepository.existsById(estabelecimentoId)) {
            throw new RecursoNaoEncontradoException("Estabelecimento não encontrado");
        }
        return repository.findByEstabelecimento_Id(estabelecimentoId).stream()
                .map(ProdutoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(UUID id) {
        return new ProdutoResponseDTO(buscarProduto(id));
    }

    @Transactional
    public void removerProduto(UUID id) {
        repository.delete(buscarProduto(id));
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(UUID id, ProdutoUpdateDTO dto) {
        Produto produto = buscarProduto(id);
        if (dto.nome() != null) {
            String nome = normalizarNome(dto.nome());
            if (!nome.equalsIgnoreCase(produto.getNome())) {
                validacaoNome.validar(nome, produto.getEstabelecimento().getId());
                produto.alterarNome(nome);
            }
        }
        if (dto.descricao() != null) produto.alterarDescricao(dto.descricao().trim());
        if (dto.marca() != null) produto.alterarMarca(dto.marca().trim());
        if (dto.categoria() != null) produto.alterarCategoria(dto.categoria().trim());
        return new ProdutoResponseDTO(produto);
    }

    private Produto buscarProduto(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    private String normalizarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }
}
