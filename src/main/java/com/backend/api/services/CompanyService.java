package com.backend.api.services;

import com.backend.api.dtos.CompanyRequestDTO;
import com.backend.api.dtos.CompanyResponseDTO;
import com.backend.api.entities.Company;
import com.backend.api.repositories.CompanyRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository;

    public CompanyService(
            CompanyRepository repository,
            PasswordEncoder passwordEncoder,
            SecurityContextRepository securityContextRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.securityContextRepository = securityContextRepository;
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

    public CompanyResponseDTO authenticate(
            String email,
            String rawPassword,
            HttpServletRequest request,
            HttpServletResponse response) {

        Company company = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(rawPassword, company.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                company.getEmail(), null, Collections.emptyList());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true);
        securityContextRepository.saveContext(context, request, response);

        return toResponse(company);
    }

    public CompanyResponseDTO findByEmail(String email) {
        Company company = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        return toResponse(company);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
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