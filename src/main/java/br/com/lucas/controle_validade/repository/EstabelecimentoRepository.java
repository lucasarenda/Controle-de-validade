package br.com.lucas.controle_validade.repository;

import br.com.lucas.controle_validade.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, UUID> {
    List<Estabelecimento> findByUser_Id(UUID id);
}
