package com.backend.api.dtos.user;

public record UserResponseDTO(Long id, String firstName, String lastName, String email, Long departmentId,
        String departmentName) {
}