package com.backend.api.enums;

public enum DiscardType {
    RECYCLING("Reciclagem"),
    INCINERATION("Incineração"),
    LANDFILL("Aterro"),
    COMPOSTING("Compostagem"),
    HAZARDOUS_WASTE("Resíduo Perigoso"),
    REUSE("Reutilização"),
    OTHER("Outro");

    private final String description;

    DiscardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
