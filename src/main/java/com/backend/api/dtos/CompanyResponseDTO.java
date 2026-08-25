package com.backend.api.dtos;

import java.time.LocalDateTime;

public record CompanyResponseDTO(
        Long id,
        String cnpj,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}