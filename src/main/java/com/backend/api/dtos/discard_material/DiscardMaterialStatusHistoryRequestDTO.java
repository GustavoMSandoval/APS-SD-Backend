package com.backend.api.dtos.discard_material;

import com.backend.api.enums.DiscardStatus;
import jakarta.validation.constraints.NotNull;

public record DiscardMaterialStatusHistoryRequestDTO(@NotNull DiscardStatus status) {
}