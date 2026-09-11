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
import br.com.lucas.controle_validade.validation.ValidacaoNomeEstabelecimentoUnico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EstabelecimentoService {
    private final UserRepository userRepository;
    private final EstabelecimentoRepository repository;
    private final ValidacaoNomeEstabelecimentoUnico validacaoNome;
    private final ValidacaoCnpjEstabelecimentoUnico validacaoCnpj;

    public EstabelecimentoService(UserRepository userRepository,
                                  EstabelecimentoRepository repository,
                                  ValidacaoNomeEstabelecimentoUnico validacaoNome,
                                  ValidacaoCnpjEstabelecimentoUnico validacaoCnpj) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.validacaoNome = validacaoNome;
        this.validacaoCnpj = validacaoCnpj;
    }

    @Transactional
    public EstabelecimentoResponseDTO cadastrarEstabelecimento(EstabelecimentoRequestDTO dto) {
        User usuario = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        String nome = normalizarNome(dto.nome());
        validacaoNome.validar(nome);
        validacaoCnpj.validar(dto.cnpj().trim());
        Estabelecimento estabelecimento = new Estabelecimento(
                nome, dto.email(), dto.cnpj().trim(), dto.telefone().trim(), dto.endereco().trim(), usuario);
        return new EstabelecimentoResponseDTO(repository.save(estabelecimento));
    }

    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> buscaEstabelecimentoPorUsuario(UUID usuarioId) {
        if (!userRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        return repository.findByUser_Id(usuarioId).stream()
                .map(EstabelecimentoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public EstabelecimentoResponseDTO buscarPorId(UUID id) {
        return new EstabelecimentoResponseDTO(buscarEstabelecimento(id));
    }

    @Transactional
    public void removeEstabelecimento(UUID id) {
        repository.delete(buscarEstabelecimento(id));
    }

    @Transactional
    public EstabelecimentoResponseDTO atualizarEstabelecimento(UUID id, EstabelecimentoUpdateDTO dto) {
        Estabelecimento estabelecimento = buscarEstabelecimento(id);
        if (dto.nome() != null) {
            String nome = normalizarNome(dto.nome());
            if (!nome.equalsIgnoreCase(estabelecimento.getNome())) {
                validacaoNome.validar(nome);
                estabelecimento.alterarNome(nome);
            }
        }
        if (dto.email() != null) estabelecimento.alterarEmail(dto.email());
        if (dto.cnpj() != null) {
            String cnpj = dto.cnpj().trim();
            if (!cnpj.equals(estabelecimento.getCnpj())) {
                validacaoCnpj.validar(cnpj);
                estabelecimento.alterarCnpj(cnpj);
            }
        }
        if (dto.telefone() != null) estabelecimento.alterarTelefone(dto.telefone().trim());
        if (dto.endereco() != null) estabelecimento.alterarEndereco(dto.endereco().trim());
        return new EstabelecimentoResponseDTO(estabelecimento);
    }

    private Estabelecimento buscarEstabelecimento(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado"));
    }

    private String normalizarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }
}
