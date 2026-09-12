package com.backend.api.dtos;

import java.util.List;

import com.backend.api.dtos.department.DepartmentDiscardSummaryDTO;
import com.backend.api.dtos.discard_material.DiscardTypeSummaryDTO;

public record DashboardResponseDTO(
        Long totalDepartments,
        Long totalDiscards,
        List<DepartmentDiscardSummaryDTO> discardsByDepartment,
        List<DiscardTypeSummaryDTO> discardsByType) {
}