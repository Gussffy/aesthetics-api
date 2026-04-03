package com.gustavo.aestheticsapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingPeriodResponseDTO(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal projectedRevenue,
        BigDecimal confirmedPayments,
        BigDecimal pendingPayments
) {
}
