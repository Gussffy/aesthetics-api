package com.gustavo.aestheticsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EstablishmentRequestDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String cnpj,
        @NotBlank String domain
) {
}
