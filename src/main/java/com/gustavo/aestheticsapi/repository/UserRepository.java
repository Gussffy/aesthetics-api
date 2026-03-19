package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Metodo para encontrar um usuário pelo email
    Optional<User> findByEmail (String email);

    // Metodo para verificar se um email já existe no banco de dados
    boolean existsByEmail(String email);
}
