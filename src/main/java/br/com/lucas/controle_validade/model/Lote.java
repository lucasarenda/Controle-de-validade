package br.com.lucas.controle_validade.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lote")
@EqualsAndHashCode(of = "id")
@Getter
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String numeroLote;
    private Integer quantidade;
    private BigDecimal custoUnitario;

    @NotNull
    private LocalDate dataEntrada;

    private LocalDate dataValidade;
    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    protected Lote() {
    }

    public Lote(String numeroLote, Integer quantidade, BigDecimal custoUnitario,
                LocalDate dataEntrada, LocalDate dataValidade, String endereco, Produto produto) {
        this.numeroLote = numeroLote;
        this.quantidade = quantidade;
        this.custoUnitario = custoUnitario;
        this.dataEntrada = dataEntrada;
        this.dataValidade = dataValidade;
        this.endereco = endereco;
        this.produto = produto;
    }

    public void alterarNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
    public void alterarQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void alterarCustoUnitario(BigDecimal custoUnitario) { this.custoUnitario = custoUnitario; }
    public void alterarDatas(LocalDate dataEntrada, LocalDate dataValidade) {
        this.dataEntrada = dataEntrada;
        this.dataValidade = dataValidade;
    }
    public void alterarEndereco(String endereco) { this.endereco = endereco; }
}
