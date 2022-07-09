package ru.example.animals.enums;

import java.util.stream.Stream;

public enum GenderType {
    MALE('m'),
    FEMALE('f'),
    UNTITLED('u');

    private final Character name;

    GenderType(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    public static GenderType getOrDefaultGenderName(GenderType type) {
        if (type == null) {
            return UNTITLED;
        }
        return Stream.of(GenderType.values())
                .filter(c -> c.getName().equals(type.getName()))
                .findFirst()
                .orElse(UNTITLED);
    }
}
