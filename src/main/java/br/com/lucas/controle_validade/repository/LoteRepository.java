package br.com.lucas.controle_validade.repository;

import br.com.lucas.controle_validade.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoteRepository extends JpaRepository<Lote, UUID> {
    List<Lote> findByProduto_Id(UUID produtoId);
}
