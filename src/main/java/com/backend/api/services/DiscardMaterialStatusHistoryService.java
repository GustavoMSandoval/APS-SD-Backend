package com.backend.api.services;

import com.backend.api.dtos.discard_material.DiscardMaterialStatusHistoryResponseDTO;
import com.backend.api.entities.DiscardMaterial;
import com.backend.api.entities.DiscardMaterialStatusHistory;
import com.backend.api.entities.User;
import com.backend.api.repositories.DiscardMaterialRepository;
import com.backend.api.repositories.DiscardMaterialStatusHistoryRepository;
import com.backend.api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscardMaterialStatusHistoryService {
    private final DiscardMaterialStatusHistoryRepository historyRepository;
    private final DiscardMaterialRepository discardMaterialRepository;
    private final UserRepository userRepository;

    public DiscardMaterialStatusHistoryService(DiscardMaterialStatusHistoryRepository historyRepository,
            DiscardMaterialRepository discardMaterialRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.discardMaterialRepository = discardMaterialRepository;
        this.userRepository = userRepository;
    }

    public DiscardMaterialStatusHistoryResponseDTO create(Long discardMaterialId, Long userId,
            com.backend.api.enums.DiscardStatus status) {
        DiscardMaterial material = discardMaterialRepository.findById(discardMaterialId)
                .orElseThrow(() -> new RuntimeException("Material não encontrado."));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        DiscardMaterialStatusHistory history = new DiscardMaterialStatusHistory();
        history.setDiscardMaterial(material);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        history.setUser(user);
        return toResponse(historyRepository.save(history));
    }

    public List<DiscardMaterialStatusHistoryResponseDTO> findByMaterial(Long discardMaterialId) {
        return historyRepository.findByDiscardMaterialIdOrderByChangedAtAsc(discardMaterialId).stream()
                .map(this::toResponse).toList();
    }

    private DiscardMaterialStatusHistoryResponseDTO toResponse(DiscardMaterialStatusHistory history) {
        User user = history.getUser();
        return new DiscardMaterialStatusHistoryResponseDTO(history.getId(), history.getStatus(), history.getChangedAt(),
                user.getId(), user.getFirstName() + " " + user.getLastName());
    }
}
