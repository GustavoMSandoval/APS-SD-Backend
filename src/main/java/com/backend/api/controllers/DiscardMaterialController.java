package com.backend.api.controllers;

import com.backend.api.dtos.DiscardMaterialRequestDTO;
import com.backend.api.dtos.DiscardMaterialResponseDTO;
import com.backend.api.services.DiscardMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/discard-materials")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class DiscardMaterialController {

    private final DiscardMaterialService service;

    public DiscardMaterialController(DiscardMaterialService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DiscardMaterialResponseDTO> create(@Valid @RequestBody DiscardMaterialRequestDTO request,
            UriComponentsBuilder ucb) {
        DiscardMaterialResponseDTO response = service.create(request);
        URI location = ucb.path("/api/discard-materials/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscardMaterialResponseDTO>> findAll() {
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