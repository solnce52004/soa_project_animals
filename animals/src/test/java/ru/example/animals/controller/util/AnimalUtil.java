package ru.example.animals.controller.util;

import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.AnimalTypeDTO;
import ru.example.animals.dto.request.PatchAnimalTypeRequestDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.enums.GenderType;

import java.sql.Timestamp;
import java.util.Date;

public final class AnimalUtil {
    public static final String USERNAME = "tester";
    public static final String BEARER_TOKEN = "Bearer token";

    public static String getRandomUsername() {
        return "test_" + Math.random();
    }

    public static String getRandomAnimalName() {
        return "pet_" + Math.random();
    }

    public static AnimalDTO initAnimal(Long id, String name) {
        final AnimalTypeDTO type = new AnimalTypeDTO()
                .setId(1L)
                .setTitle("cat");

        return new AnimalDTO()
                .setId(id)
                .setUsername(USERNAME)
                .setAnimalName(name)
                .setAnimalType(type)
                .setGender(GenderType.FEMALE)
                .setBirthdate(null);
    }

    public static PatchAnimalTypeRequestDTO initPatchAnimalTypeRequestDTO(
            Long AnimalTypeId, String AnimalType
    ) {
        final AnimalTypeDTO type = new AnimalTypeDTO()
                .setId(AnimalTypeId)
                .setTitle(AnimalType);

        return new PatchAnimalTypeRequestDTO()
                .setUsername(USERNAME)
                .setAnimalType(type);
    }

    public static AnimalDTO initPatchAnimalTypeResponseDTO(
            Long id, String name, Long AnimalTypeId, String AnimalType
    ) {
        final AnimalTypeDTO type = new AnimalTypeDTO()
                .setId(AnimalTypeId)
                .setTitle(AnimalType);

        return new AnimalDTO()
                .setId(id)
                .setUsername(USERNAME)
                .setAnimalName(name)
                .setAnimalType(type)
                .setGender(GenderType.FEMALE)
                .setBirthdate(null);
    }

    public static Animal initAnimalWithDefaultBirthdate(
            String username,
            String animalName,
            AnimalType animalType
    ) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        return new Animal()
                .setUsername(username)
                .setAnimalName(animalName)
                .setAnimalType(animalType)
                .setGender(GenderType.UNTITLED)
                .setBirthdate(date);
    }
}
