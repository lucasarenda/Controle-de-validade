package br.com.lucas.controle_validade.validation;

import br.com.lucas.controle_validade.exception.custom.UsuarioNaoPossuiEstabelecimentoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoUsuarioPossuiEstabelecimentos implements Validacao<List<Estabelecimento>> {

    @Override
    public void validar(List<Estabelecimento> estabelecimentos) {
        if (estabelecimentos.isEmpty()) {
            throw new UsuarioNaoPossuiEstabelecimentoException(
                    "Usuário não possui estabelecimentos cadastrados"
            );
        }
    }
}
