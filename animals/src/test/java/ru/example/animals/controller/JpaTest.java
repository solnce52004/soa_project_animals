package ru.example.animals.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.example.animals.entity.Animal;
import ru.example.animals.entity.AnimalType;
import ru.example.animals.enums.GenderType;
import ru.example.animals.repo.AnimalRepository;
import ru.example.animals.repo.AnimalTypeRepository;
import ru.example.animals.service.modelservice.AnimalService;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {

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
    public void helloTest() {
        System.out.println("Hello");
    }

//    @Before
//    public void resetDb() {
//        animalRepository.deleteAll();
//        animalTypeRepository.deleteAll();
//        seedAnimalType();
//    }

//    private void seedAnimalType() {
//        animalTypeRepository.saveAll(Arrays.asList(
//                new AnimalType().setTitle("cat"),
//                new AnimalType().setTitle("dog"),
//                new AnimalType().setTitle("bear")));
//    }

    @Test
    public void createAnimalTest() {
        AnimalType type = animalTypeRepository.findByTitle("cat").orElse(null);
        if (type == null) {
            type = this.testEntityManager.persistAndFlush(
                    new AnimalType().setTitle("cat"));
        }

        final Animal newAnimal = buildAnimal(
                getRandomUsername(),
                getRandomAnimalName(),
                type);

        final Animal savedAnimal = saveAnimal(newAnimal);
        this.testEntityManager.flush();

        final Animal animalById = animalRepository.findById(savedAnimal.getId())
                .orElse(null);

        if (animalById != null) {
            animalById.setCreatedAt(null).setUpdatedAt(null);
        }

        assertThat(newAnimal)
                .usingRecursiveComparison()
                .isEqualTo(animalById);
    }

    public static String getRandomUsername() {
        return "test_" + Math.random();
    }

    public String getRandomAnimalName() {
        return "pet_" + Math.random();
    }

    private Animal buildAnimal(
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

    private Animal saveAnimal(Animal animal) {
        final Long id = animalRepository.saveWithoutGender(
                animal.getUsername(),
                animal.getAnimalType().getId(),
                animal.getAnimalName(),
                animal.getBirthdate()
        );
        return animal.setId(id);
    }
}
