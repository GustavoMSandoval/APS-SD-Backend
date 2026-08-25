package com.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequestDTO(
        @NotBlank(message = "O CNPJ é obrigatório") @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres") String cnpj,

        @NotBlank(message = "O nome é obrigatório") String name,

        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido") String email,

        @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password,

        @NotBlank(message = "O telefone é obrigatório") @Size(max = 20) String phone) {
}