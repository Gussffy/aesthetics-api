package com.gustavo.aestheticsapi.dto;

import com.gustavo.aestheticsapi.domain.enums.UserRole;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        UserRole role
) {
}
