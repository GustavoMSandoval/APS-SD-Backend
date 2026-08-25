package com.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompanyLoginDTO(
        @NotBlank @Email String email,
        @NotBlank String password) {
}
