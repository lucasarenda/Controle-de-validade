package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.repository.LoteRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoNumeroLoteUnico implements Validacao<LoteRequestDTO> {
    private final LoteRepository repository;

    public ValidacaoNumeroLoteUnico(LoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(LoteRequestDTO dto) {
        if (repository.existsByNumeroLote(dto.numeroLote())) {
            throw new RecursoJaExisteException("Já existe um lote cadastrado com este número");
        }
    }
}
