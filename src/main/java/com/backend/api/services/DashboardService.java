package com.backend.api.services;

import com.backend.api.dtos.DashboardResponseDTO;
import com.backend.api.dtos.DepartmentDiscardSummaryDTO;
import com.backend.api.dtos.DiscardTypeSummaryDTO;
import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.DiscardMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final DiscardMaterialRepository discardMaterialRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardService(
            DiscardMaterialRepository discardMaterialRepository,
            DepartmentRepository departmentRepository) {
        this.discardMaterialRepository = discardMaterialRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData(
            Long companyId,
            Long departmentId,
            DiscardType type,
            DiscardStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        Long totalDepartments;
        if (departmentId != null) {
            totalDepartments = departmentRepository.existsById(departmentId) ? 1L : 0L;
        } else if (companyId != null) {
            totalDepartments = departmentRepository.countByCompanyId(companyId);
        } else {
            totalDepartments = departmentRepository.count();
        }

        Long totalDiscards = discardMaterialRepository.countTotalDiscardsInPeriod(
                companyId, departmentId, type, status, startDate, endDate);

        List<DepartmentDiscardSummaryDTO> discardsByDepartment = discardMaterialRepository.countDiscardsByDepartment(
                companyId, type, status, startDate, endDate);

        List<DiscardTypeSummaryDTO> discardsByType = discardMaterialRepository.countDiscardsByType(
                companyId, departmentId, status, startDate, endDate);

        return new DashboardResponseDTO(
                totalDepartments,
                totalDiscards,
                discardsByDepartment,
                discardsByType);
    }
}