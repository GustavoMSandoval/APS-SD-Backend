package com.backend.api.controllers;

import com.backend.api.dtos.department.DepartmentRequestDTO;
import com.backend.api.dtos.department.DepartmentResponseDTO;
import com.backend.api.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:4200"
}, allowCredentials = "true")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(
            @Valid @RequestBody DepartmentRequestDTO request,
            Authentication authentication,
            UriComponentsBuilder ucb) {

        DepartmentResponseDTO response = service.create(request, authentication);

        URI location = ucb
                .path("/api/departments/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> findAll(
            Authentication authentication) {

        return ResponseEntity.ok(
                service.findAll(authentication));
    }

    @GetMapping("/search")
    public ResponseEntity<DepartmentResponseDTO> findByName(
            @RequestParam("name") String name,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.findByName(name, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        if (service.delete(id, authentication)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}