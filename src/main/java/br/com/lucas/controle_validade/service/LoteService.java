package br.com.lucas.controle_validade.service;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.response.LoteResponseDTO;
import br.com.lucas.controle_validade.exception.custom.ProdutoNaoPossuiLotesException;
import br.com.lucas.controle_validade.exception.custom.RecursoNaoEncontradoException;
import br.com.lucas.controle_validade.model.Lote;
import br.com.lucas.controle_validade.model.Produto;
import br.com.lucas.controle_validade.model.StatusValidade;
import br.com.lucas.controle_validade.repository.LoteRepository;
import br.com.lucas.controle_validade.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class LoteService {
    private final LoteRepository repository;
    private final ProdutoRepository produtoRepository;

    public LoteService(LoteRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    public LoteResponseDTO cadastrarLote(LoteRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        return converter(repository.save(new Lote(dto, produto)));
    }

    public List<LoteResponseDTO> buscaLotesPorProduto(UUID produtoId) {
        List<Lote> lotes = repository.findByProduto_Id(produtoId);
        if (lotes.isEmpty()) {
            throw new ProdutoNaoPossuiLotesException("Produto não possui lotes cadastrados");
        }
        return lotes.stream().map(this::converter).toList();
    }

    public LoteResponseDTO buscaLotePorId(UUID id) {
        return converter(buscarLote(id));
    }

    public void removerLote(UUID id) {

        repository.delete(buscarLote(id));
    }

    public long calcularDiasParaVencimento(Lote lote) {
        return ChronoUnit.DAYS.between(LocalDate.now(), lote.getDataValidade());
    }

    public StatusValidade calcularStatus(Lote lote) {
        long dias = calcularDiasParaVencimento(lote);
        if (dias < 0) return StatusValidade.VENCIDO;
        if (dias <= 4) return StatusValidade.CRITICO;
        if (dias <= 7) return StatusValidade.PROXIMO_VENCIMENTO;
        return StatusValidade.NORMAL;
    }

    private Lote buscarLote(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lote não encontrado"));
    }

    private LoteResponseDTO converter(Lote lote) {
        return new LoteResponseDTO(lote, calcularDiasParaVencimento(lote), calcularStatus(lote));
    }
}
