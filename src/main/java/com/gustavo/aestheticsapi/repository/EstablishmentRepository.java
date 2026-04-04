package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

    Optional<Establishment> findByEmail(String email);

    Optional<Establishment> findByDomain(String domain);

    boolean existsByEmail(String email);
}
