package com.backend.api.dtos;

import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DiscardMaterialRequestDTO(
        @NotNull(message = "O tipo de descarte é obrigatório") DiscardType type,

        @Size(max = 500) String description,

        @NotNull(message = "A data de descarte é obrigatória") LocalDateTime discardDate,

        @NotBlank(message = "O nome do funcionário é obrigatório") String employeeWhoDiscarded,

        @NotNull(message = "O status do descarte é obrigatório") DiscardStatus status,

        @NotNull(message = "O ID do departamento é obrigatório") Long departmentId) {
}