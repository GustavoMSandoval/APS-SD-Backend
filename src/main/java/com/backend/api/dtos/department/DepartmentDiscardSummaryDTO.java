package com.backend.api.dtos.department;

public record DepartmentDiscardSummaryDTO(
        String departmentName,
        Long totalDiscards) {
}