package com.gustavo.aestheticsapi.dto;

import jakarta.validation.constraints.NotNull;

public record BranchAestheticServiceRequestDTO(
        @NotNull Long branchId,
        @NotNull Long serviceId
) {
}
