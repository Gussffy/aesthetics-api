package com.gustavo.aestheticsapi.repository;

import com.gustavo.aestheticsapi.domain.entity.BranchAestheticService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchAestheticServiceRepository extends JpaRepository<BranchAestheticService, Long> {

    List<BranchAestheticService> findByBranchIdAndAvailableTrue(Long branchId);

    Optional<BranchAestheticService> findByBranchIdAndServiceId(Long branchId, Long serviceId);

    List<BranchAestheticService> findByBranchId(Long branchId);
}
