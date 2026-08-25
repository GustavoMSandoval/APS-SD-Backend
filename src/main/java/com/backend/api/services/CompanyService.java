package com.backend.api.services;

import com.backend.api.dtos.CompanyRequestDTO;
import com.backend.api.dtos.CompanyResponseDTO;
import com.backend.api.entities.Company;
import com.backend.api.repositories.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private CompanyResponseDTO toResponse(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getCnpj(),
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getCreatedAt(),
                company.getUpdatedAt());
    }

    public CompanyResponseDTO create(CompanyRequestDTO request) {
        Company company = new Company();

        company.setCnpj(request.cnpj());
        company.setName(request.name());
        company.setEmail(request.email());
        company.setPassword(passwordEncoder.encode(request.password()));
        company.setPhone(request.phone());

        return toResponse(repository.save(company));
    }

    public CompanyResponseDTO authenticate(String email, String rawPassword) {
        Company company = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(rawPassword, company.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return toResponse(company);
    }

    public List<CompanyResponseDTO> findAll() {
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