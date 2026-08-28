package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.ProdutoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoNomeProdutoUnico implements Validacao<ProdutoRequestDTO> {
    private final ProdutoRepository repository;

    public ValidacaoNomeProdutoUnico(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(ProdutoRequestDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new RecursoJaExisteException("Já existe um produto cadastrado com este nome");
        }
    }
}
