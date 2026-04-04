package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.AestheticService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AestheticeServiceRepository extends JpaRepository<AestheticService, Long> {

    // Metodo para encontrar todos os serviços ativos
    List<AestheticService> findByActiveTrue();

    List<AestheticService> findByEstablishmentId (Long establishmentId);

    List<AestheticService> findByEstablishmentIdAndActiveTrue (Long branchId);
}
