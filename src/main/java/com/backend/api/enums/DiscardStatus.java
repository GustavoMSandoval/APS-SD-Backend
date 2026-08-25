package com.backend.api.enums;

public enum DiscardStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Concluído"),
    CANCELLED("Cancelado"),
    ON_HOLD("Suspenso");

    private final String description;

    DiscardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}