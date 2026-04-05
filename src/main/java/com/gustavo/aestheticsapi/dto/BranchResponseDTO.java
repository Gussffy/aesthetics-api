package com.gustavo.aestheticsapi.dto;

public record BranchResponseDTO(
        Long id,
        Long establishmentId,
        String branchCode,
        String name,
        String address,
        String subdomain,
        Boolean active
) {
}
