package ru.example.animals.enums;

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
}
