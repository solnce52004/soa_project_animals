package ru.example.animals.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.repo.AnimalRepository;
import ru.example.animals.repo.AnimalTypeRepository;
import ru.example.animals.service.modelservice.AnimalService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaTest {

    private static final String ANIMAL_TYPE_DOG = "dog";
    private static final String ANIMAL_TYPE_CAT = "cat";
    @MockBean
    AnimalService animalService;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private AnimalTypeRepository animalTypeRepository;
    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void pingDbTest() {
        assertThat(animalRepository).isNotNull();
        assertThat(animalTypeRepository).isNotNull();
    }

    @Test
    public void testAnimalExistsExceptionIsThrown() {
        final Animal animal1 = buildAnimalWithPersistedType(
                "user_1", "Tom_1", ANIMAL_TYPE_CAT);
        final Animal animal2 = buildAnimalWithPersistedType(
                "user_1", "Tom_1", ANIMAL_TYPE_CAT);

        AssertionsForClassTypes.assertThatExceptionOfType(
                DataIntegrityViolationException.class).isThrownBy(
                () -> {
                    saveAnimal(animal1);
                    saveAnimal(animal2);
                });
    }

    @Test
    public void createAnimalTest() {
        final Animal newAnimal = buildAnimalWithPersistedType(
                AnimalUtil.getRandomUsername(),
                AnimalUtil.getRandomAnimalName(),
                ANIMAL_TYPE_DOG);

        final Animal savedAnimal = saveAnimal(newAnimal);
//        this.testEntityManager.flush();

        final Animal animalById = animalRepository.findById(savedAnimal.getId())
                .orElse(null);

        if (animalById != null) {
            animalById.setCreatedAt(null).setUpdatedAt(null);
        }

        assertThat(newAnimal)
                .usingRecursiveComparison()
                .isEqualTo(animalById);
    }

    private Animal buildAnimalWithPersistedType(
            String username,
            String animalName,
            String animalType
    ) {
        AnimalType type = animalTypeRepository.findByTitle(animalType).orElseGet(
                () -> this.testEntityManager.persistAndFlush(
                        new AnimalType().setTitle(animalType)));

        return AnimalUtil.initAnimalWithDefaultBirthdate(username, animalName, type);
    }

    private Animal saveAnimal(Animal animal) {
        final Long id = animalRepository.saveByParams(
                animal.getUsername(),
                animal.getAnimalType().getId(),
                animal.getAnimalName(),
                animal.getGender().getName(),
                animal.getBirthdate()
        );
        return animal.setId(id);
    }
}
