package ru.example.animals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application.properties"})
//@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class AnimalsApplicationTests {

    @Test
    void contextLoads() {
    }

}
