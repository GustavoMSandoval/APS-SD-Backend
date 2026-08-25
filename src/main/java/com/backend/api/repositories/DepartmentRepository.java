package com.backend.api.repositories;

import com.backend.api.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByCompanyId(Long companyId);

    Page<Department> findByCompanyId(Long companyId, Pageable pageable);

    Optional<Department> findByName(String name);
    Optional<Department> findByNameAndCompanyId(String name, Long companyId);

    List<Department> findByCompanyIdAndNameContainingIgnoreCase(Long companyId, String name);

    boolean existsByNameAndCompanyId(String name, Long companyId);

    Long countByCompanyId(Long companyId);
}