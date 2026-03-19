package com.gustavo.aestheticsapi.dto;

import com.gustavo.aestheticsapi.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,
        String clientName,
        String serviceName,
        LocalDateTime scheduledAt,
        AppointmentStatus status
) {
}
