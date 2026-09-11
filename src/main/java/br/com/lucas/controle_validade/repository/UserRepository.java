package br.com.lucas.controle_validade.repository;


import br.com.lucas.controle_validade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByNomeIgnoreCase(String nome);
}
