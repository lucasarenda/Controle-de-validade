package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCnpjEstabelecimentoUnico {
    private final EstabelecimentoRepository repository;

    public ValidacaoCnpjEstabelecimentoUnico(EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    public void validar(String cnpj) {
        if (repository.existsByCnpj(cnpj)) {
            throw new RecursoJaExisteException("Já existe outro estabelecimento com esse CNPJ");
        }
    }
}
