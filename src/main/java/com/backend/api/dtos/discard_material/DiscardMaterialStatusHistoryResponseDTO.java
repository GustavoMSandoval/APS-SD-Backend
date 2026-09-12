package com.backend.api.dtos.discard_material;

import com.backend.api.enums.DiscardStatus;
import java.time.LocalDateTime;

public record DiscardMaterialStatusHistoryResponseDTO(Long id, DiscardStatus status, LocalDateTime changedAt,
        Long userId, String userName) {
}