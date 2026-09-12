package com.backend.api.dtos.discard_material;

import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiscardMaterialResponseDTO(Long id, DiscardType type, String description, LocalDateTime discardDate,
                BigDecimal weight, Long userId, String userName, DiscardStatus status, Long departmentId,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
}