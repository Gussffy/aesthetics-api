package com.gustavo.aestheticsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AestheticServiceRequestDTO(
        @NotBlank String name,
        String description,
        @NotBlank String category,
        @NotNull BigDecimal price,
        @NotNull Integer durationMinutes

) {
}
