package com.gustavo.aestheticsapi.dto;

public record BranchAestheticServiceResponseDTO(
        Long id,
        Long branchId,
        Long serviceId,
        String serviceName,
        Boolean available
) {
}
