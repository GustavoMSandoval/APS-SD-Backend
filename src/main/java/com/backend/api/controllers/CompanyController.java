package com.backend.api.controllers;

import com.backend.api.dtos.CompanyLoginDTO;
import com.backend.api.dtos.CompanyRequestDTO;
import com.backend.api.dtos.CompanyResponseDTO;
import com.backend.api.services.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO request,
            UriComponentsBuilder ucb) {
        CompanyResponseDTO response = service.create(request);
        URI location = ucb.path("/api/companies/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CompanyResponseDTO> login(@Valid @RequestBody CompanyLoginDTO loginRequest) {
        CompanyResponseDTO response = service.authenticate(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}