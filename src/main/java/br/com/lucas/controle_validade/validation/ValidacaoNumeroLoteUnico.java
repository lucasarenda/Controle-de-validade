package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.LoteRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidacaoNumeroLoteUnico {
    private final LoteRepository repository;

    public ValidacaoNumeroLoteUnico(LoteRepository repository) {
        this.repository = repository;
    }

    public void validar(String numeroLote, UUID produtoId) {
        if (repository.existsByNumeroLoteIgnoreCaseAndProduto_Id(numeroLote, produtoId)) {
            throw new RecursoJaExisteException("Já existe um lote com este número neste produto");
        }
    }
}
