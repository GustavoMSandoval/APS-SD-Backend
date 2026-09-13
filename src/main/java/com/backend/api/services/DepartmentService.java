package com.backend.api.services;

import com.backend.api.dtos.department.DepartmentRequestDTO;
import com.backend.api.dtos.department.DepartmentResponseDTO;
import com.backend.api.entities.Company;
import com.backend.api.entities.Department;
import com.backend.api.entities.User;
import com.backend.api.repositories.CompanyRepository;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public DepartmentService(
            DepartmentRepository repository,
            CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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

    private Long getAuthenticatedId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado.");
        }
        return Long.valueOf(authentication.getName());
    }

    private User getAuthenticatedUser(Authentication authentication) {
        Long userId = getAuthenticatedId(authentication);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    private Company getAuthenticatedCompany(Authentication authentication) {
        Long companyId = getAuthenticatedId(authentication);

        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada."));
    }

    // Validação corrigida: Verifica se o ID autenticado existe na tabela de
    // Empresas
    private boolean isCompany(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Long id = Long.valueOf(authentication.getName());
        return companyRepository.existsById(id);
    }

    // Validação corrigida: Verifica se o ID autenticado existe na tabela de
    // Usuários comuns
    private boolean isUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Long id = Long.valueOf(authentication.getName());
        return userRepository.existsById(id);
    }

    public DepartmentResponseDTO create(
            DepartmentRequestDTO request,
            Authentication authentication) {

        if (!isCompany(authentication)) {
            throw new RuntimeException(
                    "Apenas a empresa pode criar departamentos.");
        }

        Company company = getAuthenticatedCompany(authentication);

        if (!company.getId().equals(request.companyId())) {
            throw new RuntimeException(
                    "Você não pode criar um departamento para outra empresa.");
        }

        Department department = new Department();
        department.setName(request.name());
        department.setDescription(request.description());
        department.setResponsibleName(request.responsibleName());
        department.setCompany(company);

        return toResponse(repository.save(department));
    }

    public DepartmentResponseDTO findByName(
            String name,
            Authentication authentication) {

        if (isCompany(authentication)) {
            Company company = getAuthenticatedCompany(authentication);

            Department department = repository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado."));

            if (!department.getCompany().getId().equals(company.getId())) {
                throw new RuntimeException(
                        "Você não tem acesso a este departamento.");
            }

            return toResponse(department);
        }

        if (isUser(authentication)) {
            User user = getAuthenticatedUser(authentication);

            Department department = repository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado."));

            if (!department.getId().equals(user.getDepartment().getId())) {
                throw new RuntimeException(
                        "Você não tem acesso a este departamento.");
            }

            return toResponse(department);
        }

        throw new RuntimeException("Tipo de autenticação inválido.");
    }

    public List<DepartmentResponseDTO> findAll(
            Authentication authentication) {

        if (isCompany(authentication)) {
            Company company = getAuthenticatedCompany(authentication);

            return repository.findAll()
                    .stream()
                    .filter(department -> department.getCompany().getId()
                            .equals(company.getId()))
                    .map(this::toResponse)
                    .toList();
        }

        if (isUser(authentication)) {
            User user = getAuthenticatedUser(authentication);

            return List.of(
                    toResponse(user.getDepartment()));
        }

        throw new RuntimeException("Tipo de autenticação inválido.");
    }

    public boolean delete(
            Long id,
            Authentication authentication) {

        if (!isCompany(authentication)) {
            throw new RuntimeException(
                    "Apenas a empresa pode excluir departamentos.");
        }

        Company company = getAuthenticatedCompany(authentication);

        Department department = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado."));

        if (!department.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException(
                    "Você não pode excluir um departamento de outra empresa.");
        }

        repository.delete(department);

        return true;
    }
}