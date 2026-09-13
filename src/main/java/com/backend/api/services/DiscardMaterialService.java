package com.backend.api.services;

import com.backend.api.dtos.discard_material.DiscardMaterialRequestDTO;
import com.backend.api.dtos.discard_material.DiscardMaterialResponseDTO;
import com.backend.api.entities.Department;
import com.backend.api.entities.DiscardMaterial;
import com.backend.api.entities.User;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.DiscardMaterialRepository;
import com.backend.api.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscardMaterialService {

    private final DiscardMaterialRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DiscardMaterialService(
            DiscardMaterialRepository repository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    private DiscardMaterialResponseDTO toResponse(
            DiscardMaterial discardMaterial) {

        return new DiscardMaterialResponseDTO(
                discardMaterial.getId(),
                discardMaterial.getType(),
                discardMaterial.getDescription(),
                discardMaterial.getDiscardDate(),
                discardMaterial.getWeight(),
                discardMaterial.getUser().getId(),
                discardMaterial.getUser().getFirstName()
                        + " "
                        + discardMaterial.getUser().getLastName(),
                discardMaterial.getStatus(),
                discardMaterial.getDepartment().getId(),
                discardMaterial.getCreatedAt(),
                discardMaterial.getUpdatedAt());
    }

    private Long getAuthenticatedUserId(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado.");
        }

        return Long.valueOf(authentication.getName());
    }

    private User getAuthenticatedUser(
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public DiscardMaterialResponseDTO create(
            DiscardMaterialRequestDTO request,
            Authentication authentication) {

        User authenticatedUser = getAuthenticatedUser(authentication);

        if (!authenticatedUser.getId().equals(request.userId())) {
            throw new RuntimeException(
                    "Você não pode registrar um descarte para outro usuário.");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Departamento não encontrado."));

        if (!department.getId().equals(
                authenticatedUser.getDepartment().getId())) {

            throw new RuntimeException(
                    "Você não pode registrar descarte em outro departamento.");
        }

        User user = authenticatedUser;

        if (!user.getDepartment().getId().equals(
                department.getId())) {

            throw new RuntimeException(
                    "O usuário não pertence a este departamento.");
        }

        DiscardMaterial discardMaterial = new DiscardMaterial();

        discardMaterial.setType(request.type());
        discardMaterial.setDescription(request.description());
        discardMaterial.setDiscardDate(request.discardDate());
        discardMaterial.setWeight(request.weight());
        discardMaterial.setUser(user);
        discardMaterial.setStatus(request.status());
        discardMaterial.setDepartment(department);

        return toResponse(
                repository.save(discardMaterial));
    }

    public List<DiscardMaterialResponseDTO> findAll(
            Authentication authentication) {

        User authenticatedUser = getAuthenticatedUser(authentication);

        Long departmentId = authenticatedUser.getDepartment().getId();

        return repository.findAll()
                .stream()
                .filter(discardMaterial -> discardMaterial.getDepartment()
                        .getId()
                        .equals(departmentId))
                .map(this::toResponse)
                .toList();
    }

    public boolean delete(
            Long id,
            Authentication authentication) {

        User authenticatedUser = getAuthenticatedUser(authentication);

        DiscardMaterial discardMaterial = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Material não encontrado."));

        Long userDepartmentId = authenticatedUser.getDepartment().getId();

        Long materialDepartmentId = discardMaterial.getDepartment().getId();

        if (!userDepartmentId.equals(materialDepartmentId)) {
            throw new RuntimeException(
                    "Você não pode excluir material de outro departamento.");
        }

        repository.delete(discardMaterial);

        return true;
    }
}