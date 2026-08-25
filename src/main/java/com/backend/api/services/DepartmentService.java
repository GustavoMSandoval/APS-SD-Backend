package com.backend.api.services;

import com.backend.api.dtos.DepartmentRequestDTO;
import com.backend.api.dtos.DepartmentResponseDTO;
import com.backend.api.entities.Company;
import com.backend.api.entities.Department;
import com.backend.api.repositories.CompanyRepository;
import com.backend.api.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;
    private final CompanyRepository companyRepository;

    public DepartmentService(DepartmentRepository repository, CompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    private DepartmentResponseDTO toResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getResponsibleName(),
                department.getCompany().getId(),
                department.getCreatedAt(),
                department.getUpdatedAt());
    }

    public DepartmentResponseDTO create(DepartmentRequestDTO request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Department department = new Department();
        department.setName(request.name());
        department.setDescription(request.description());
        department.setResponsibleName(request.responsibleName());
        department.setCompany(company);

        return toResponse(repository.save(department));
    }

    public DepartmentResponseDTO findByName(String name) {
        Department department = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
        return toResponse(department);
    }

    public List<DepartmentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}