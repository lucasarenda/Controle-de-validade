package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.exception.custom.UsuarioNaoPossuiEstabelecimentoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstabelecimentoService {
    private final UserRepository repositoryUser;
    private final EstabelecimentoRepository repositoryEstabelecimento;

    public EstabelecimentoService(
            UserRepository repositoryUser,
            EstabelecimentoRepository repositoryEstabelecimento
    ) {
        this.repositoryUser = repositoryUser;
        this.repositoryEstabelecimento = repositoryEstabelecimento;
    }

    public EstabelecimentoResponseDTO cadastrarEstabelecimento(EstabelecimentoRequestDTO dto) {
        User usuario = repositoryUser.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Estabelecimento estabelecimento = new Estabelecimento(dto, usuario);
        Estabelecimento estabelecimentoSalvo = repositoryEstabelecimento.save(estabelecimento);

        return new EstabelecimentoResponseDTO(estabelecimentoSalvo);
    }

    public List<EstabelecimentoResponseDTO> buscaEstabelecimentoPorUsuario(UUID id) {
        List<Estabelecimento> estabelecimentos = repositoryEstabelecimento.findByUser_Id(id);

        if (estabelecimentos.isEmpty()) {
            throw new UsuarioNaoPossuiEstabelecimentoException(
                    "Usuário não possui estabelecimentos cadastrados"
            );
        }

        return estabelecimentos.stream()
                .map(EstabelecimentoResponseDTO::new)
                .toList();
    }

    public void removeEstabelecimento(UUID id) {
        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));

        repositoryEstabelecimento.delete(estabelecimento);
    }
}
