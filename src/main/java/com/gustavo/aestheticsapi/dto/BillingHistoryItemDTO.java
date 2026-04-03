package com.gustavo.aestheticsapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingHistoryItemDTO(
        LocalDate date,
        BigDecimal total
) {
}
