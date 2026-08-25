package com.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres") String description,

        @NotBlank(message = "O nome do responsável é obrigatório") String responsibleName,

        @NotNull(message = "O ID da empresa é obrigatório") Long companyId) {
}