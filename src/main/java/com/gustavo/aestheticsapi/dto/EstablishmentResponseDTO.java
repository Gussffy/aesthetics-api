package com.gustavo.aestheticsapi.dto;

public record EstablishmentResponseDTO(
        Long id,
        String name,
        String email,
        String cnpj,
        String domain,
        Boolean active
) {
}
