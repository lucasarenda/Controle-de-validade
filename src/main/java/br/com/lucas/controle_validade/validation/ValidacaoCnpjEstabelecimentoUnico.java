package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoJaExisteException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCnpjEstabelecimentoUnico implements Validacao<EstabelecimentoRequestDTO> {

    private final EstabelecimentoRepository repository;

    public ValidacaoCnpjEstabelecimentoUnico(EstabelecimentoRepository repository){
        this.repository = repository;
    }
    @Override
    public void validar(EstabelecimentoRequestDTO dto){
        validar(dto.cnpj());
    }
    public void validar(String cnpj){
        if(repository.existsByCnpj(cnpj)){
            throw new RecursoJaExisteException("Já existe outro estabelecimento com esse cnpj");
        }
    }
}
