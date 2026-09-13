package com.backend.api.controllers;

import com.backend.api.dtos.discard_material.*;
import com.backend.api.services.DiscardMaterialStatusHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discard-materials/{discardMaterialId}/history")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:4200"
}, allowCredentials = "true")
public class DiscardMaterialStatusHistoryController {

    private final DiscardMaterialStatusHistoryService service;

    public DiscardMaterialStatusHistoryController(
            DiscardMaterialStatusHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DiscardMaterialStatusHistoryResponseDTO> create(
            @PathVariable Long discardMaterialId,
            @Valid @RequestBody DiscardMaterialStatusHistoryRequestDTO request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Long userId = Long.valueOf(authentication.getName());

        return ResponseEntity.ok(
                service.create(
                        discardMaterialId,
                        userId,
                        request.status()));
    }

    @GetMapping
    public ResponseEntity<List<DiscardMaterialStatusHistoryResponseDTO>> findByMaterial(
            @PathVariable Long discardMaterialId) {

        return ResponseEntity.ok(
                service.findByMaterial(discardMaterialId));
    }
}