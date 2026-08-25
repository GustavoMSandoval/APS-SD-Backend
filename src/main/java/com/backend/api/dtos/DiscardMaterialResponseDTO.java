package com.backend.api.dtos;

import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;

import java.time.LocalDateTime;

public record DiscardMaterialResponseDTO(
        Long id,
        DiscardType type,
        String description,
        LocalDateTime discardDate,
        String employeeWhoDiscarded,
        DiscardStatus status,
        Long departmentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}