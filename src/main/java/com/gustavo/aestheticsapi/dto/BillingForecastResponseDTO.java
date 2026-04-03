package com.gustavo.aestheticsapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingForecastResponseDTO(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal total
) {
}
