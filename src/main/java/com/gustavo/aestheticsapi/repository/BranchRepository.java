package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByEstablishmentId(Long establishmentId);

    Optional<Branch> findBySubdomain(String subdomain);

    Optional<Branch> findByEstablishmentIdAndBranchCode(Long establishmentId, String branchCode);

}
