package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidacoesDuplicidadeTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EstabelecimentoRepository estabelecimentoRepository;

    @Mock
    private LoteRepository loteRepository;

    @Test
    void deveImpedirProdutoComNomeJaCadastrado() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz", "Arroz branco", "Marca", "Alimento", UUID.randomUUID()
        );
        when(produtoRepository.existsByNome(dto.nome())).thenReturn(true);

        RecursoJaExisteException exception = assertThrows(
                RecursoJaExisteException.class,
                () -> new ValidacaoNomeProdutoUnico(produtoRepository).validar(dto)
        );

        assertEquals("Já existe um produto cadastrado com este nome", exception.getMessage());
    }

    @Test
    void deveImpedirEstabelecimentoComNomeJaCadastrado() {
        EstabelecimentoRequestDTO dto = new EstabelecimentoRequestDTO(
                "Mercado", "mercado@email.com", "123", "9999", "Rua A", UUID.randomUUID()
        );
        when(estabelecimentoRepository.existsByNome(dto.nome())).thenReturn(true);

        RecursoJaExisteException exception = assertThrows(
                RecursoJaExisteException.class,
                () -> new ValidacaoNomeEstabelecimentoUnico(estabelecimentoRepository).validar(dto)
        );

        assertEquals("Já existe um estabelecimento cadastrado com este nome", exception.getMessage());
    }

    @Test
    void deveImpedirLoteComNumeroJaCadastrado() {
        LoteRequestDTO dto = new LoteRequestDTO(
                "LOTE-1", 1, BigDecimal.ONE, LocalDate.now(), LocalDate.now().plusDays(1),
                "Prateleira A", UUID.randomUUID()
        );
        when(loteRepository.existsByNumeroLote(dto.numeroLote())).thenReturn(true);

        RecursoJaExisteException exception = assertThrows(
                RecursoJaExisteException.class,
                () -> new ValidacaoNumeroLoteUnico(loteRepository).validar(dto)
        );

        assertEquals("Já existe um lote cadastrado com este número", exception.getMessage());
    }
}
