package com.backend.api.controllers;

import com.backend.api.dtos.discard_material.DiscardMaterialRequestDTO;
import com.backend.api.dtos.discard_material.DiscardMaterialResponseDTO;
import com.backend.api.services.DiscardMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/discard-materials")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:4200"
}, allowCredentials = "true")
public class DiscardMaterialController {

    private final DiscardMaterialService service;

    public DiscardMaterialController(DiscardMaterialService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DiscardMaterialResponseDTO> create(
            @Valid @RequestBody DiscardMaterialRequestDTO request,
            Authentication authentication,
            UriComponentsBuilder ucb) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        DiscardMaterialResponseDTO response = service.create(request, authentication);

        URI location = ucb
                .path("/api/discard-materials/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscardMaterialResponseDTO>> findAll(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                service.findAll(authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        if (service.delete(id, authentication)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}