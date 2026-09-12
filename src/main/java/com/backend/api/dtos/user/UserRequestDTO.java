package com.backend.api.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(@NotBlank String firstName, @NotBlank String lastName, @Email @NotBlank String email,
        @NotBlank String password, @NotNull Long departmentId) {
}
