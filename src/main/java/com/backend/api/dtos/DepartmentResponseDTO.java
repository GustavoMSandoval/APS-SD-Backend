package com.backend.api.dtos;

import java.time.LocalDateTime;

public record DepartmentResponseDTO(
        Long id,
        String name,
        String description,
        String responsibleName,
        Long companyId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}