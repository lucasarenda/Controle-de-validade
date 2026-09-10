package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.EstabelecimentoRequestDTO;
import br.com.lucas.controle_validade.Dto.request.EstabelecimentoUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.EstabelecimentoResponseDTO;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Estabelecimento;
import br.com.lucas.controle_validade.model.User;
import br.com.lucas.controle_validade.repository.EstabelecimentoRepository;
import br.com.lucas.controle_validade.repository.UserRepository;
import br.com.lucas.controle_validade.validation.ValidacaoCnpjEstabelecimentoUnico;
import br.com.lucas.controle_validade.validation.ValidacaoUsuarioPossuiEstabelecimentos;
import br.com.lucas.controle_validade.validation.ValidacaoNomeEstabelecimentoUnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstabelecimentoService {
    @Autowired
    private  UserRepository repositoryUser;

    @Autowired
    private  EstabelecimentoRepository repositoryEstabelecimento;

    @Autowired
    private  ValidacaoUsuarioPossuiEstabelecimentos validacaoUsuarioPossuiEstabelecimentos;

    @Autowired
    private  ValidacaoNomeEstabelecimentoUnico validacaoNomeEstabelecimentoUnico;

    @Autowired
    private ValidacaoCnpjEstabelecimentoUnico validacaoCnpjEstabelecimentoUnico;


    public EstabelecimentoResponseDTO cadastrarEstabelecimento(EstabelecimentoRequestDTO dto) {
        User usuario = repositoryUser.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        String nomeNormalizado = dto.nome()
                .trim()
                .replaceAll("\\s+", " ");

        validacaoNomeEstabelecimentoUnico.validar(nomeNormalizado);
        validacaoCnpjEstabelecimentoUnico.validar(dto.cnpj());

        Estabelecimento estabelecimento = new Estabelecimento(
                nomeNormalizado,
                dto.cnpj(),
                dto.email(),
                dto.telefone(),
                dto.endereco(),
                usuario
        );

        Estabelecimento estabelecimentoSalvo = repositoryEstabelecimento.save(estabelecimento);

        return new EstabelecimentoResponseDTO(estabelecimentoSalvo);
    }

    public List<EstabelecimentoResponseDTO> buscaEstabelecimentoPorUsuario(UUID id) {
        List<Estabelecimento> estabelecimentos = repositoryEstabelecimento.findByUser_Id(id);

        validacaoUsuarioPossuiEstabelecimentos.validar(estabelecimentos);

        return estabelecimentos.stream()
                .map(EstabelecimentoResponseDTO::new)
                .toList();
    }

    public void removeEstabelecimento(UUID id) {

        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));

        repositoryEstabelecimento.delete(estabelecimento);
    }

    public EstabelecimentoResponseDTO atualizarEstabelecimento(UUID id, EstabelecimentoUpdateDTO dto) {
        Estabelecimento estabelecimento = repositoryEstabelecimento.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));

        if (dto.nome() != null && !dto.nome().equalsIgnoreCase(estabelecimento.getNome())) {
            validacaoNomeEstabelecimentoUnico.validar(dto.nome());
            estabelecimento.setNome(dto.nome());
        }

        if (dto.nome() != null){
            estabelecimento.setNome(dto.nome());
        }

        if (dto.email() != null) {
            estabelecimento.setEmail(dto.email().trim().toLowerCase());
        }

        if (dto.cnpj() != null && !dto.cnpj().equalsIgnoreCase(estabelecimento.getCnpj())){
            validacaoCnpjEstabelecimentoUnico.validar(dto.cnpj());
            estabelecimento.setCnpj(dto.cnpj());
        }

        if (dto.telefone() != null){
            estabelecimento.setTelefone(dto.telefone());
        }

        if (dto.endereco() != null) {
           estabelecimento.setEndereco(dto.endereco());
        }
        return new EstabelecimentoResponseDTO(repositoryEstabelecimento.save(estabelecimento));
    }
}
