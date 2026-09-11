package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.request.LoteUpdateDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.exception.custom.DataLoteInvalidaException;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import br.com.lucas.controle_validade.validation.ValidacaoNumeroLoteUnico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class LoteService {
    static final int LIMITE_DIAS_CRITICO = 4;
    static final int LIMITE_DIAS_PROXIMO_VENCIMENTO = 7;

    private final LoteRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ValidacaoNumeroLoteUnico validacaoNumeroLote;
    private final Clock clock;

    public LoteService(LoteRepository repository, ProdutoRepository produtoRepository,
                       ValidacaoNumeroLoteUnico validacaoNumeroLote, Clock clock) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.validacaoNumeroLote = validacaoNumeroLote;
        this.clock = clock;
    }

    @Transactional
    public LoteResponseDTO cadastrarLote(LoteRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        validarDatas(dto.dataEntrada(), dto.dataValidade());
        String numeroLote = dto.numeroLote().trim();
        validacaoNumeroLote.validar(numeroLote, dto.produtoId());
        Lote lote = new Lote(numeroLote, dto.quantidade(), dto.custoUnitario(), dto.dataEntrada(),
                dto.dataValidade(), dto.endereco().trim(), produto);
        return converter(repository.save(lote));
    }

    @Transactional(readOnly = true)
    public List<LoteResponseDTO> buscaLotesPorProduto(UUID produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado");
        }
        return repository.findByProduto_Id(produtoId).stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public LoteResponseDTO buscaLotePorId(UUID id) {
        return converter(buscarLote(id));
    }

    @Transactional
    public void removerLote(UUID id) {
        repository.delete(buscarLote(id));
    }

    @Transactional
    public LoteResponseDTO atualizarLote(UUID id, LoteUpdateDTO dto) {
        Lote lote = buscarLote(id);
        if (dto.numeroLote() != null) {
            String numero = dto.numeroLote().trim();
            if (!numero.equalsIgnoreCase(lote.getNumeroLote())) {
                validacaoNumeroLote.validar(numero, lote.getProduto().getId());
                lote.alterarNumeroLote(numero);
            }
        }

        LocalDate dataEntrada = dto.dataEntrada() != null ? dto.dataEntrada() : lote.getDataEntrada();
        LocalDate dataValidade = dto.dataValidade() != null ? dto.dataValidade() : lote.getDataValidade();
        validarDatas(dataEntrada, dataValidade);
        if (dto.dataEntrada() != null || dto.dataValidade() != null) {
            lote.alterarDatas(dataEntrada, dataValidade);
        }
        if (dto.quantidade() != null) lote.alterarQuantidade(dto.quantidade());
        if (dto.custoUnitario() != null) lote.alterarCustoUnitario(dto.custoUnitario());
        if (dto.endereco() != null) lote.alterarEndereco(dto.endereco().trim());
        return converter(lote);
    }

    long calcularDiasParaVencimento(Lote lote, LocalDate dataReferencia) {
        return ChronoUnit.DAYS.between(dataReferencia, lote.getDataValidade());
    }

    StatusValidade calcularStatus(long dias) {
        if (dias < 0) return StatusValidade.VENCIDO;
        if (dias <= LIMITE_DIAS_CRITICO) return StatusValidade.CRITICO;
        if (dias <= LIMITE_DIAS_PROXIMO_VENCIMENTO) return StatusValidade.PROXIMO_VENCIMENTO;
        return StatusValidade.NORMAL;
    }

    private void validarDatas(LocalDate dataEntrada, LocalDate dataValidade) {
        if (dataValidade.isBefore(dataEntrada)) {
            throw new DataLoteInvalidaException(
                    "Data de validade não pode ser anterior à data de entrada");
        }
    }

    private Lote buscarLote(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lote não encontrado"));
    }

    private LoteResponseDTO converter(Lote lote) {
        LocalDate hoje = LocalDate.now(clock);
        long dias = calcularDiasParaVencimento(lote, hoje);
        return new LoteResponseDTO(lote, dias, calcularStatus(dias));
    }
}
