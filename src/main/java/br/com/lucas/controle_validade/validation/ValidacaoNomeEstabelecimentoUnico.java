package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoNomeEstabelecimentoUnico implements Validacao<EstabelecimentoRequestDTO> {
    private final EstabelecimentoRepository repository;

    public ValidacaoNomeEstabelecimentoUnico(EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(EstabelecimentoRequestDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new RecursoJaExisteException("Já existe um estabelecimento cadastrado com este nome");
        }
    }
}
