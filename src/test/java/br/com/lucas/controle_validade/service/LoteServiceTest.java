package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.request.LoteUpdateDTO;
import br.com.lucas.controle_validade.exception.custom.DataLoteInvalidaException;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoNumeroLoteUnico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {
    @Mock LoteRepository repository;
    @Mock ProdutoRepository produtoRepository;
    @Mock ValidacaoNumeroLoteUnico validacaoNumero;
    private LoteService service;
    private final LocalDate hoje = LocalDate.of(2026, 9, 11);

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-09-11T12:00:00Z"), ZoneOffset.UTC);
        service = new LoteService(repository, produtoRepository, validacaoNumero, clock);
    }

    @Test
    void rejeitaDatasInvalidasNoCadastro() {
        UUID produtoId = UUID.randomUUID();
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(mock(Produto.class)));
        var dto = new LoteRequestDTO("L1", 1, BigDecimal.ONE,
                hoje, hoje.minusDays(1), "A", produtoId);
        assertThrows(DataLoteInvalidaException.class, () -> service.cadastrarLote(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void unicidadeDoCadastroUsaProduto() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = mock(Produto.class);
        when(produto.getId()).thenReturn(produtoId);
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        service.cadastrarLote(new LoteRequestDTO(" L1 ", 1, BigDecimal.ONE,
                hoje, hoje.plusDays(8), "A", produtoId));
        verify(validacaoNumero).validar("L1", produtoId);
    }

    @Test
    void produtoExistenteSemLotesRetornaListaVazia() {
        UUID produtoId = UUID.randomUUID();
        when(produtoRepository.existsById(produtoId)).thenReturn(true);
        when(repository.findByProduto_Id(produtoId)).thenReturn(List.of());
        assertTrue(service.buscaLotesPorProduto(produtoId).isEmpty());
    }

    @Test
    void produtoInexistenteRetornaErro() {
        UUID produtoId = UUID.randomUUID();
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscaLotesPorProduto(produtoId));
    }

    @Test
    void patchSomenteDataValidadeComparaComEntradaExistente() {
        UUID id = UUID.randomUUID();
        Lote lote = lote(hoje, hoje.plusDays(10));
        when(repository.findById(id)).thenReturn(Optional.of(lote));
        assertThrows(DataLoteInvalidaException.class, () -> service.atualizarLote(id,
                new LoteUpdateDTO(null, null, null, null, hoje.minusDays(1), null)));
    }

    @Test
    void patchSomenteDataEntradaComparaComValidadeExistente() {
        UUID id = UUID.randomUUID();
        Lote lote = lote(hoje.minusDays(5), hoje.plusDays(1));
        when(repository.findById(id)).thenReturn(Optional.of(lote));
        assertThrows(DataLoteInvalidaException.class, () -> service.atualizarLote(id,
                new LoteUpdateDTO(null, null, null, hoje.plusDays(2), null, null)));
    }

    @Test
    void updateComMesmoNumeroNaoValidaDuplicidade() {
        UUID id = UUID.randomUUID();
        Lote lote = lote(hoje, hoje.plusDays(8));
        when(repository.findById(id)).thenReturn(Optional.of(lote));
        service.atualizarLote(id, new LoteUpdateDTO(" l1 ", 2, null, null, null, null));
        verifyNoInteractions(validacaoNumero);
        assertEquals(2, lote.getQuantidade());
    }

    @Test
    void classificaStatusNosLimites() {
        assertEquals(StatusValidade.VENCIDO, service.calcularStatus(-1));
        assertEquals(StatusValidade.CRITICO, service.calcularStatus(0));
        assertEquals(StatusValidade.CRITICO, service.calcularStatus(4));
        assertEquals(StatusValidade.PROXIMO_VENCIMENTO, service.calcularStatus(5));
        assertEquals(StatusValidade.PROXIMO_VENCIMENTO, service.calcularStatus(7));
        assertEquals(StatusValidade.NORMAL, service.calcularStatus(8));
    }

    @Test
    void calculaDiasComDataDeReferenciaDeterministica() {
        assertEquals(8, service.calcularDiasParaVencimento(lote(hoje, hoje.plusDays(8)), hoje));
    }

    private Lote lote(LocalDate entrada, LocalDate validade) {
        return new Lote("L1", 1, BigDecimal.ONE, entrada, validade, "A", mock(Produto.class));
    }
}
