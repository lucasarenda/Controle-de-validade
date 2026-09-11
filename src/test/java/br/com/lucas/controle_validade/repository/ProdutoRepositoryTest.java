package br.com.lucas.controle_validade.repository;

import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoRepositoryTest {
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void retornaTrueQuandoExisteProdutoComMesmoNomeNoMesmoEstabelecimento() {
        Estabelecimento estabelecimento = persistirEstabelecimento("1");
        persistirProduto("Arroz", estabelecimento);

        boolean existe = produtoRepository.existsByNomeIgnoreCaseAndEstabelecimento_Id(
                "Arroz", estabelecimento.getId());

        assertThat(existe).isTrue();
    }

    @Test
    void ignoraDiferencaEntreMaiusculasEMinusculas() {
        Estabelecimento estabelecimento = persistirEstabelecimento("2");
        persistirProduto("Arroz Integral", estabelecimento);

        boolean existe = produtoRepository.existsByNomeIgnoreCaseAndEstabelecimento_Id(
                "ARROZ INTEGRAL", estabelecimento.getId());

        assertThat(existe).isTrue();
    }

    @Test
    void retornaFalseQuandoMesmoNomeExisteEmOutroEstabelecimento() {
        Estabelecimento primeiro = persistirEstabelecimento("3");
        Estabelecimento segundo = persistirEstabelecimento("4");
        persistirProduto("Feijão", primeiro);

        boolean existe = produtoRepository.existsByNomeIgnoreCaseAndEstabelecimento_Id(
                "Feijão", segundo.getId());

        assertThat(existe).isFalse();
    }

    private Estabelecimento persistirEstabelecimento(String sufixo) {
        User user = new User("Usuário " + sufixo, "usuario" + sufixo + "@email.com", "123456");
        entityManager.persist(user);
        Estabelecimento estabelecimento = new Estabelecimento(
                "Mercado " + sufixo, "mercado" + sufixo + "@email.com", "cnpj-" + sufixo,
                "9999", "Rua A", user);
        entityManager.persist(estabelecimento);
        entityManager.flush();
        return estabelecimento;
    }

    private void persistirProduto(String nome, Estabelecimento estabelecimento) {
        entityManager.persist(new Produto(nome, "Descrição", "Marca", "Categoria", estabelecimento));
        entityManager.flush();
    }
}
