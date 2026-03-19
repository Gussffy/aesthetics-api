package com.gustavo.aestheticsapi.dto;

import com.gustavo.aestheticsapi.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDTO(
        @NotNull Long appointmentId,
        @NotNull PaymentMethod paymentMethod
) {
}
