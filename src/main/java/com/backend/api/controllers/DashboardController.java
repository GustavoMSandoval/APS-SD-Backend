package com.backend.api.controllers;

import com.backend.api.dtos.DashboardResponseDTO;
import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import com.backend.api.services.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" }, allowCredentials = "true")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) DiscardType type,
            @RequestParam(required = false) DiscardStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Long companyId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DashboardResponseDTO dashboard = dashboardService.getDashboardData(
                companyId, departmentName, type, status, startDate, endDate);

        return ResponseEntity.ok(dashboard);
    }
}