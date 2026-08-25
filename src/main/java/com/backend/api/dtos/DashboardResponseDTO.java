package com.backend.api.dtos;

import java.util.List;

public record DashboardResponseDTO(
        Long totalDepartments,
        Long totalDiscards,
        List<DepartmentDiscardSummaryDTO> discardsByDepartment,
        List<DiscardTypeSummaryDTO> discardsByType) {
}