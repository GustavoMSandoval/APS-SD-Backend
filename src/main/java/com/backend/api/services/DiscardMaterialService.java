package com.backend.api.services;

import com.backend.api.dtos.DiscardMaterialRequestDTO;
import com.backend.api.dtos.DiscardMaterialResponseDTO;
import com.backend.api.entities.Department;
import com.backend.api.entities.DiscardMaterial;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.DiscardMaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscardMaterialService {

    private final DiscardMaterialRepository repository;
    private final DepartmentRepository departmentRepository;

    public DiscardMaterialService(DiscardMaterialRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    private DiscardMaterialResponseDTO toResponse(DiscardMaterial discardMaterial) {
        return new DiscardMaterialResponseDTO(
                discardMaterial.getId(),
                discardMaterial.getType(),
                discardMaterial.getDescription(),
                discardMaterial.getDiscardDate(),
                discardMaterial.getEmployeeWhoDiscarded(),
                discardMaterial.getStatus(),
                discardMaterial.getDepartment().getId(),
                discardMaterial.getCreatedAt(),
                discardMaterial.getUpdatedAt());
    }

    public DiscardMaterialResponseDTO create(DiscardMaterialRequestDTO request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        DiscardMaterial discardMaterial = new DiscardMaterial();
        discardMaterial.setType(request.type());
        discardMaterial.setDescription(request.description());
        discardMaterial.setDiscardDate(request.discardDate());
        discardMaterial.setEmployeeWhoDiscarded(request.employeeWhoDiscarded());
        discardMaterial.setStatus(request.status());
        discardMaterial.setDepartment(department);

        return toResponse(repository.save(discardMaterial));
    }

    public List<DiscardMaterialResponseDTO> findAll() {
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