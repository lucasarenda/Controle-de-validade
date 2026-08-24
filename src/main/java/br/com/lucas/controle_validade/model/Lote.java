package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lote")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String numeroLote;
    @NotNull
    private Integer quantidade;
    @NotNull
    private BigDecimal custoUnitario;
    @NotNull
    private LocalDate dataEntrada;
    @NotNull
    private LocalDate dataValidade;
    @NotBlank
    private String endereco;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Lote(LoteRequestDTO dto, Produto produto) {
        this.numeroLote = dto.numeroLote();
        this.quantidade = dto.quantidade();
        this.custoUnitario = dto.custoUnitario();
        this.dataEntrada = dto.dataEntrada();
        this.dataValidade = dto.dataValidade();
        this.endereco = dto.endereco();
        this.produto = produto;
    }
}
