package br.com.lucas.controle_validade.repository;


import br.com.lucas.controle_validade.model.Produto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByEstabelecimento_Id(UUID id);
    boolean existsByNomeIgnoreCase(String nome);
}
