package com.backend.api.dtos;

import com.backend.api.enums.DiscardType;

public record DiscardTypeSummaryDTO(
        DiscardType type,
        Long totalDiscards) {
}