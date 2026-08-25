package com.backend.api.repositories;

import com.backend.api.entities.DiscardMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscardMaterialRepository extends JpaRepository<DiscardMaterial, Long> {

    List<DiscardMaterial> findByDepartmentId(Long departmentId);
}