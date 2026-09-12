package com.backend.api.services;

import com.backend.api.dtos.discard_material.DiscardMaterialRequestDTO;
import com.backend.api.dtos.discard_material.DiscardMaterialResponseDTO;
import com.backend.api.entities.Department;
import com.backend.api.entities.DiscardMaterial;
import com.backend.api.entities.User;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.DiscardMaterialRepository;
import com.backend.api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscardMaterialService {
    private final DiscardMaterialRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DiscardMaterialService(DiscardMaterialRepository repository, DepartmentRepository departmentRepository,
            UserRepository userRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    private DiscardMaterialResponseDTO toResponse(DiscardMaterial discardMaterial) {
        return new DiscardMaterialResponseDTO(discardMaterial.getId(), discardMaterial.getType(),
                discardMaterial.getDescription(), discardMaterial.getDiscardDate(), discardMaterial.getWeight(),
                discardMaterial.getUser().getId(),
                discardMaterial.getUser().getFirstName() + " " + discardMaterial.getUser().getLastName(),
                discardMaterial.getStatus(), discardMaterial.getDepartment().getId(), discardMaterial.getCreatedAt(),
                discardMaterial.getUpdatedAt());
    }

    public DiscardMaterialResponseDTO create(DiscardMaterialRequestDTO request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        DiscardMaterial discardMaterial = new DiscardMaterial();
        discardMaterial.setType(request.type());
        discardMaterial.setDescription(request.description());
        discardMaterial.setDiscardDate(request.discardDate());
        discardMaterial.setWeight(request.weight());
        discardMaterial.setUser(user);
        discardMaterial.setStatus(request.status());
        discardMaterial.setDepartment(department);
        return toResponse(repository.save(discardMaterial));
    }

    public List<DiscardMaterialResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}