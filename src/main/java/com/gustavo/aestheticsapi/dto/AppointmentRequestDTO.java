package com.gustavo.aestheticsapi.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequestDTO(
        @NotNull Long clientId,
        @NotNull Long serviceId,
        @NotNull Long establishmentId,
        @NotNull Long branchId,
        @NotNull LocalDateTime scheduledAt
) {
}
