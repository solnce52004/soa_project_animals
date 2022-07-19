package ru.example.animals.service.modelservice;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.example.animals.config.container.ContainersEnvironment;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.repo.AnimalRepository;
import ru.example.animals.repo.AnimalTypeRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest extends ContainersEnvironment {

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

    @BeforeClass
    public static void setupContainer() {
        postgreSQLContainer.start();
        applyMigrate(postgreSQLContainer);
    }

    @AfterClass
    public static void shutdown() {
        cleanMigrate();
        postgreSQLContainer.stop();
    }

    @Test
    public void pingDbTest() {
        assertThat(animalRepository).isNotNull();
        assertThat(animalTypeRepository).isNotNull();
    }

    @Test
    @Transactional
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
        this.testEntityManager.flush();

        final Animal animalByName = animalRepository.findByAnimalName(savedAnimal.getAnimalName());

        if (animalByName != null) {
            animalByName.setCreatedAt(null).setUpdatedAt(null);
        }

        assertThat(animalByName).isNotNull();
        assertThat(newAnimal)
                .usingRecursiveComparison()
                .isEqualTo(animalByName);
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
