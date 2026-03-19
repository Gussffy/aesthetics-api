package com.gustavo.aestheticsapi.dto;

import java.math.BigDecimal;

public record AestheticServiceResponseDTO(
        Long id,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer durationInMinutes,
        Boolean active

) {
}
