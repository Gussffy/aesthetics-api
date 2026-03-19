package com.gustavo.aestheticsapi.dto;

import com.gustavo.aestheticsapi.domain.enums.PaymentMethod;
import com.gustavo.aestheticsapi.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,
        Long appointmentId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        LocalDateTime paidAt
) {
}
