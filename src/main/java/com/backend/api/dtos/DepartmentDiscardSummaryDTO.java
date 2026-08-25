package com.backend.api.dtos;

public record DepartmentDiscardSummaryDTO(
        String departmentName,
        Long totalDiscards) {
}