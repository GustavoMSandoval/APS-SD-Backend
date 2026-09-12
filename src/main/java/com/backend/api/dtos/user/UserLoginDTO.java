package com.backend.api.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@Email @NotBlank String email, @NotBlank String password) {
}