package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoProdutoPossuiLotes;
import br.com.lucas.controle_validade.validation.ValidacaoNumeroLoteUnico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ValidacaoProdutoPossuiLotes validacaoProdutoPossuiLotes;

    @Mock
    private ValidacaoNumeroLoteUnico validacaoNumeroLoteUnico;

    @InjectMocks
    private LoteService service;

    @Mock
    private Produto produto;

    @Test
    void calcularStatusVencido() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().minusDays(1));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.VENCIDO,status);
    }

    @Test
    void calcularStatusCritico() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(4));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.CRITICO,status);
    }

    @Test
    void calcularStatusProximoDaValidade() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(7));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.PROXIMO_VENCIMENTO,status);
    }

    @Test
    void calcularStatusNormal() {

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(8));

        StatusValidade status = service.calcularStatus(lote);
        assertEquals(StatusValidade.NORMAL,status);
    }
    @Test
    void deveCadastrarLote() {
        // Arrange
        LoteRequestDTO dto = new LoteRequestDTO(
                "2",
                1,
                new BigDecimal("9.99"),
                LocalDate.of(2026, 8, 28),
                LocalDate.of(2027, 8, 28),
                "casado joao",
                produto.getId()
        );

        BDDMockito.given(produtoRepository.findById(dto.produtoId()))
                .willReturn(Optional.of(produto));

        BDDMockito.given(loteRepository.save(BDDMockito.any(Lote.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // Act
        LoteResponseDTO resultado = service.cadastrarLote(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(dto.produtoId(), resultado.produtoId());

        BDDMockito.then(produtoRepository)
                .should()
                .findById(dto.produtoId());

        BDDMockito.then(loteRepository)
                .should()
                .save(BDDMockito.any(Lote.class));
    }

    @Test
    void deveBuscarLotePorId() {

        // Arrange
        UUID id = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Arroz");

        Lote lote = new Lote();
        lote.setDataValidade(LocalDate.now().plusDays(10));
        lote.setProduto(produto);

        when(loteRepository.findById(id))
                .thenReturn(Optional.of(lote));

        // Act
        LoteResponseDTO resultado = service.buscaLotePorId(id);

        // Assert
        assertNotNull(resultado);

        verify(loteRepository).findById(id);

    }

}
