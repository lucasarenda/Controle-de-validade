package br.com.lucas.controle_validade.model;

import br.com.lucas.controle_validade.Dto.request.LoteRequestDTO;
import br.com.lucas.controle_validade.Dto.request.LoteUpdateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(unique = true)
    private String numeroLote;

    private Integer quantidade;

    private BigDecimal custoUnitario;

    @NotNull
    private LocalDate dataEntrada;

    private LocalDate dataValidade;

    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public void atualizar(LoteUpdateDTO dto) {
        if (dto.numeroLote() != null) this.numeroLote = dto.numeroLote();
        if (dto.quantidade() != null) this.quantidade = dto.quantidade();
        if (dto.custoUnitario() != null) this.custoUnitario = dto.custoUnitario();
        if (dto.dataEntrada() != null) this.dataEntrada = dto.dataEntrada();
        if (dto.dataValidade() != null) this.dataValidade = dto.dataValidade();
        if (dto.endereco() != null) this.endereco = dto.endereco();
    }
}
