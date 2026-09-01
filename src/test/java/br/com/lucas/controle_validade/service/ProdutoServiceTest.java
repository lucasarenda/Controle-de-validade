package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoEstabelecimentoPossuiProdutos;
import br.com.lucas.controle_validade.validation.ValidacaoNomeProdutoUnico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock
    private EstabelecimentoRepository repositoryEstabelecimento;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ValidacaoEstabelecimentoPossuiProdutos validacaoEstabelecimentoPossuiProdutos;

    @Mock
    private ValidacaoNomeProdutoUnico validacaoNomeProdutoUnico;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deveCadastrarProduto() {
        UUID estabelecimentoId = UUID.randomUUID();
        var estabelecimento = new Estabelecimento();
        var dto = new ProdutoRequestDTO(
                "Arroz",
                "Branco",
                "Marca",
                "Alimento",
                estabelecimentoId
        );
        when(repositoryEstabelecimento.findById(estabelecimentoId))
                .thenReturn(Optional.of(estabelecimento));
        when(repository.save(any(Produto.class)))
                .thenAnswer(i -> i.getArgument(0));

        var resultado = service.cadastrarProduto(dto);

        assertEquals("Arroz", resultado.nome());
        verify(validacaoNomeProdutoUnico).validar(dto);
        verify(repository).save(any(Produto.class));
    }

    @Test
    void deveBuscarProdutosPorEstabelecimento() {
        UUID estabelecimentoId = UUID.randomUUID();
        var estabelecimento = new Estabelecimento();
        var produto = new Produto(
                UUID.randomUUID(),
                "Arroz",
                "Branco",
                "Marca",
                "Alimento",
                estabelecimento,
                LocalDateTime.now(),
                List.of()
        );
        when(repository.findByEstabelecimento_Id(estabelecimentoId))
                .thenReturn(List.of(produto));

        var resultado = service.buscaProdutosPorEstabelecimento(estabelecimentoId);

        assertEquals(1, resultado.size());
        assertEquals("Arroz", resultado.get(0).nome());
        verify(validacaoEstabelecimentoPossuiProdutos).validar(List.of(produto));
    }

    @Test
    void deveRemoverProduto() {
        UUID id = UUID.randomUUID();
        var produto = new Produto();
        when(repository.findById(id))
                .thenReturn(Optional.of(produto));

        service.removerProduto(id);

        verify(repository).delete(produto);
    }

    @Test
    void deveFalharAoRemoverProdutoInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.removerProduto(id));
        verify(repository, never()).delete(any());
    }
}
