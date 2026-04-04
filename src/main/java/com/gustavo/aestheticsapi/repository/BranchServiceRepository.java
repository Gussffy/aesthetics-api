package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.BranchService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchServiceRepository extends JpaRepository<BranchService, Long> {

    List<BranchService> findByBranchIdAndAvailableTrue(Long branchId);

    Optional<BranchService> findByBranchIdAndServiceId(Long branchId, Long serviceId);

    List<BranchService> findByBranchId(Long branchId);
}
