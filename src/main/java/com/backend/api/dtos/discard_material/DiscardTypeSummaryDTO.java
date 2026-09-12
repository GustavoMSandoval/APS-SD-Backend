package com.backend.api.dtos.discard_material;

import com.backend.api.enums.DiscardType;

public record DiscardTypeSummaryDTO(
        DiscardType type,
        Long totalDiscards) {
}