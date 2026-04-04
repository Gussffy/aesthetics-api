package com.gustavo.aestheticsapi.dto;


import com.gustavo.aestheticsapi.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(

        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull UserRole role,
        @NotNull Long establishmentId,
        Long branchId
) {
}
