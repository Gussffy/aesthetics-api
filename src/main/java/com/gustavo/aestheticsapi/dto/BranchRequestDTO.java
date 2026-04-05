package com.gustavo.aestheticsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BranchRequestDTO(
        @NotNull Long establishmentId,
        @NotBlank String branchCode,
        @NotBlank String name,
        @NotBlank  String address,
        @NotBlank  String subdomain
) {
}
