package com.backend.api.repositories;

import com.backend.api.dtos.DepartmentDiscardSummaryDTO;
import com.backend.api.dtos.DiscardTypeSummaryDTO;
import com.backend.api.entities.DiscardMaterial;
import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscardMaterialRepository extends JpaRepository<DiscardMaterial, Long> {

    List<DiscardMaterial> findByDepartmentId(Long departmentId);

    @Query("SELECT d FROM DiscardMaterial d " +
            "WHERE (:companyId IS NULL OR d.department.company.id = :companyId) " +
            "AND (:departmentId IS NULL OR d.department.id = :departmentId) " +
            "AND (:type IS NULL OR d.type = :type) " +
            "AND (:status IS NULL OR d.status = :status) " +
            "AND (cast(:startDate as timestamp) IS NULL OR d.discardDate >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR d.discardDate <= :endDate)")
    List<DiscardMaterial> filterDiscards(
            @Param("companyId") Long companyId,
            @Param("departmentId") Long departmentId,
            @Param("type") DiscardType type,
            @Param("status") DiscardStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.backend.api.dtos.DepartmentDiscardSummaryDTO(d.department.name, COUNT(d)) " +
            "FROM DiscardMaterial d " +
            "WHERE (:companyId IS NULL OR d.department.company.id = :companyId) " +
            "AND (:type IS NULL OR d.type = :type) " +
            "AND (:status IS NULL OR d.status = :status) " +
            "AND (cast(:startDate as timestamp) IS NULL OR d.discardDate >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR d.discardDate <= :endDate) " +
            "GROUP BY d.department.name")
    List<DepartmentDiscardSummaryDTO> countDiscardsByDepartment(
            @Param("companyId") Long companyId,
            @Param("type") DiscardType type,
            @Param("status") DiscardStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.backend.api.dtos.DiscardTypeSummaryDTO(d.type, COUNT(d)) " +
            "FROM DiscardMaterial d " +
            "WHERE (:companyId IS NULL OR d.department.company.id = :companyId) " +
            "AND (:departmentId IS NULL OR d.department.id = :departmentId) " +
            "AND (:status IS NULL OR d.status = :status) " +
            "AND (cast(:startDate as timestamp) IS NULL OR d.discardDate >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR d.discardDate <= :endDate) " +
            "GROUP BY d.type")
    List<DiscardTypeSummaryDTO> countDiscardsByType(
            @Param("companyId") Long companyId,
            @Param("departmentId") Long departmentId,
            @Param("status") DiscardStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(d) FROM DiscardMaterial d " +
            "WHERE (:companyId IS NULL OR d.department.company.id = :companyId) " +
            "AND (:departmentId IS NULL OR d.department.id = :departmentId) " +
            "AND (:type IS NULL OR d.type = :type) " +
            "AND (:status IS NULL OR d.status = :status) " +
            "AND (cast(:startDate as timestamp) IS NULL OR d.discardDate >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR d.discardDate <= :endDate)")
    Long countTotalDiscardsInPeriod(
            @Param("companyId") Long companyId,
            @Param("departmentId") Long departmentId,
            @Param("type") DiscardType type,
            @Param("status") DiscardStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}