package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.model.Estabelecimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lote")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor

public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String numeroLote;

    @NotBlank
    private Integer quantidade;

    @NotBlank
    private BigDecimal custoUnitario;

    @NotBlank
    private LocalDate dataEntrada;

    @NotBlank
    private LocalDate dataValidade;

    @NotBlank
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Lote(String numeroLote, Integer quantidade, BigDecimal custoUnitario, LocalDate dataEntrada, LocalDate dataValidade, String endereco) {
        this.numeroLote = numeroLote;
        this.quantidade = quantidade;
        this.custoUnitario = custoUnitario;
        this.dataEntrada = dataEntrada;
        this.dataValidade = dataValidade;
        this.endereco = endereco;
    }
}
