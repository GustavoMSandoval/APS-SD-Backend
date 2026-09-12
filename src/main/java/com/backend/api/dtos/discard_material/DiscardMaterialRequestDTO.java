package com.backend.api.dtos.discard_material;

import com.backend.api.enums.DiscardStatus;
import com.backend.api.enums.DiscardType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiscardMaterialRequestDTO(@NotNull(message = "O tipo de descarte é obrigatório") DiscardType type,
                @Size(max = 500) String description,
                @NotNull(message = "A data de descarte é obrigatória") LocalDateTime discardDate,
                @NotNull(message = "O peso é obrigatório") BigDecimal weight,
                @NotNull(message = "O ID do usuário é obrigatório") Long userId,
                @NotNull(message = "O status do descarte é obrigatório") DiscardStatus status,
                @NotNull(message = "O ID do departamento é obrigatório") Long departmentId) {
}