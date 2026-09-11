package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidacaoNomeProdutoUnico {
    private final ProdutoRepository repository;

    public ValidacaoNomeProdutoUnico(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void validar(String nome, UUID estabelecimentoId) {
        if (repository.existsByNomeIgnoreCaseAndEstabelecimento_Id(nome, estabelecimentoId)) {
            throw new RecursoJaExisteException(
                    "Já existe um produto com este nome neste estabelecimento");
        }
    }
}
