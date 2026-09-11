package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoUpdateDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoNomeProdutoUnico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock EstabelecimentoRepository estabelecimentoRepository;
    @Mock ProdutoRepository repository;
    @Mock ValidacaoNomeProdutoUnico validacaoNome;
    @InjectMocks ProdutoService service;

    @Test
    void unicidadeDoCadastroUsaEstabelecimento() {
        UUID estabelecimentoId = UUID.randomUUID();
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getId()).thenReturn(estabelecimentoId);
        when(estabelecimentoRepository.findById(estabelecimentoId)).thenReturn(Optional.of(estabelecimento));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        var resposta = service.cadastrarProduto(
                new ProdutoRequestDTO(" Arroz ", "Branco", "Marca", "Alimento", estabelecimentoId));
        assertEquals("Arroz", resposta.nome());
        verify(validacaoNome).validar("Arroz", estabelecimentoId);
    }

    @Test
    void estabelecimentoExistenteSemProdutosRetornaListaVazia() {
        UUID id = UUID.randomUUID();
        when(estabelecimentoRepository.existsById(id)).thenReturn(true);
        when(repository.findByEstabelecimento_Id(id)).thenReturn(List.of());
        assertTrue(service.buscaProdutosPorEstabelecimento(id).isEmpty());
    }

    @Test
    void estabelecimentoInexistenteRetornaErro() {
        UUID id = UUID.randomUUID();
        when(estabelecimentoRepository.existsById(id)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscaProdutosPorEstabelecimento(id));
    }

    @Test
    void updateComMesmoNomeNaoValidaDuplicidade() {
        UUID id = UUID.randomUUID();
        Produto produto = produto();
        when(repository.findById(id)).thenReturn(Optional.of(produto));
        service.atualizarProduto(id, new ProdutoUpdateDTO(" arroz ", "Integral", null, null));
        verifyNoInteractions(validacaoNome);
        assertEquals("Integral", produto.getDescricao());
    }

    @Test
    void updateComNovoNomeValidaNoMesmoEstabelecimento() {
        UUID id = UUID.randomUUID();
        UUID estabelecimentoId = UUID.randomUUID();
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getId()).thenReturn(estabelecimentoId);
        Produto produto = new Produto("Arroz", "Branco", "Marca", "Alimento", estabelecimento);
        when(repository.findById(id)).thenReturn(Optional.of(produto));
        service.atualizarProduto(id, new ProdutoUpdateDTO("Feijão", null, null, null));
        verify(validacaoNome).validar("Feijão", estabelecimentoId);
    }

    @Test
    void updateTentandoDuplicarNomePropagaErro() {
        UUID id = UUID.randomUUID();
        UUID estabelecimentoId = UUID.randomUUID();
        Estabelecimento estabelecimento = mock(Estabelecimento.class);
        when(estabelecimento.getId()).thenReturn(estabelecimentoId);
        Produto produto = new Produto("Arroz", "Branco", "Marca", "Alimento", estabelecimento);
        when(repository.findById(id)).thenReturn(Optional.of(produto));
        doThrow(new RecursoJaExisteException("duplicado"))
                .when(validacaoNome).validar("Feijão", estabelecimentoId);
        assertThrows(RecursoJaExisteException.class,
                () -> service.atualizarProduto(id, new ProdutoUpdateDTO("Feijão", null, null, null)));
        assertEquals("Arroz", produto.getNome());
    }

    private Produto produto() {
        return new Produto("Arroz", "Branco", "Marca", "Alimento", mock(Estabelecimento.class));
    }
}
