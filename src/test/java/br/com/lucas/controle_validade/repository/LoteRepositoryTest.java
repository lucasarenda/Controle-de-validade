package br.com.lucas.controle_validade.repository;

import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LoteRepositoryTest {
    @Autowired LoteRepository loteRepository;
    @Autowired EntityManager entityManager;

    @Test
    void retornaTrueQuandoLoteExisteParaMesmoProduto() {
        Produto produto = persistirProduto("1");
        persistirLote("LOTE-10", produto);

        boolean existe = loteRepository.existsByNumeroLoteIgnoreCaseAndProduto_Id(
                "LOTE-10", produto.getId());

        assertThat(existe).isTrue();
    }

    @Test
    void ignoraDiferencaEntreMaiusculasEMinusculas() {
        Produto produto = persistirProduto("2");
        persistirLote("Lote-AbC", produto);

        boolean existe = loteRepository.existsByNumeroLoteIgnoreCaseAndProduto_Id(
                "lote-abc", produto.getId());

        assertThat(existe).isTrue();
    }

    @Test
    void retornaFalseQuandoMesmoNumeroLotePertenceAOutroProduto() {
        Produto primeiro = persistirProduto("3");
        Produto segundo = persistirProduto("4");
        persistirLote("LOTE-20", primeiro);

        boolean existe = loteRepository.existsByNumeroLoteIgnoreCaseAndProduto_Id(
                "LOTE-20", segundo.getId());

        assertThat(existe).isFalse();
    }

    private Produto persistirProduto(String sufixo) {
        User user = new User("Usuário " + sufixo, "lote.usuario" + sufixo + "@email.com", "123456");
        entityManager.persist(user);
        Estabelecimento estabelecimento = new Estabelecimento(
                "Mercado " + sufixo, "lote.mercado" + sufixo + "@email.com", "lote-cnpj-" + sufixo,
                "9999", "Rua A", user);
        entityManager.persist(estabelecimento);
        Produto produto = new Produto("Produto " + sufixo, "Descrição", "Marca", "Categoria", estabelecimento);
        entityManager.persist(produto);
        entityManager.flush();
        return produto;
    }

    private void persistirLote(String numeroLote, Produto produto) {
        entityManager.persist(new Lote(numeroLote, 10, BigDecimal.ONE,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 1), "A", produto));
        entityManager.flush();
    }
}
