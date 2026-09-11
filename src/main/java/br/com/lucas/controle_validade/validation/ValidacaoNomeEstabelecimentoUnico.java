package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoNomeEstabelecimentoUnico {
    private final EstabelecimentoRepository repository;

    public ValidacaoNomeEstabelecimentoUnico(EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    public void validar(String nome) {
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new RecursoJaExisteException("Já existe um estabelecimento cadastrado com este nome");
        }
    }
}
