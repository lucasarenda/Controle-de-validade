package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ValidacoesDuplicidadeTest {
    @Test
    void produtoPodeRepetirNomeEmOutroEstabelecimento() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        UUID estabelecimentoId = UUID.randomUUID();
        when(repository.existsByNomeIgnoreCaseAndEstabelecimento_Id("Arroz", estabelecimentoId))
                .thenReturn(false);
        assertDoesNotThrow(() -> new ValidacaoNomeProdutoUnico(repository)
                .validar("Arroz", estabelecimentoId));
    }

    @Test
    void produtoNaoPodeRepetirNomeNoMesmoEstabelecimento() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        UUID estabelecimentoId = UUID.randomUUID();
        when(repository.existsByNomeIgnoreCaseAndEstabelecimento_Id("Arroz", estabelecimentoId))
                .thenReturn(true);
        assertThrows(RecursoJaExisteException.class,
                () -> new ValidacaoNomeProdutoUnico(repository).validar("Arroz", estabelecimentoId));
    }

    @Test
    void loteNaoPodeRepetirNumeroNoMesmoProduto() {
        LoteRepository repository = mock(LoteRepository.class);
        UUID produtoId = UUID.randomUUID();
        when(repository.existsByNumeroLoteIgnoreCaseAndProduto_Id("L1", produtoId)).thenReturn(true);
        assertThrows(RecursoJaExisteException.class,
                () -> new ValidacaoNumeroLoteUnico(repository).validar("L1", produtoId));
    }
}
