package ru.example.animals.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.AnimalTypeDTO;
import ru.example.animals.enums.GenderType;
import ru.example.animals.service.api.VerifyAccessTokenService;
import ru.example.animals.service.modelservice.AnimalService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {AnimalController.class})
class AnimalControllerMockTest {

    private static final String USERNAME_VERIFIED = "rty";
    private static final String BEARER_TOKEN = "Bearer token";

    @Autowired
    MockMvc mvc;
    @MockBean
    AnimalService animalService;
    @MockBean
    VerifyAccessTokenService verifyAccessTokenService;

    @BeforeEach
    void verifyRequest() {
        Mockito.when(verifyAccessTokenService.verifyToken(BEARER_TOKEN))
                .thenReturn(USERNAME_VERIFIED);
    }

    private static AnimalDTO initAnimal(Long id, String name) {
        final AnimalTypeDTO type = new AnimalTypeDTO()
                .setId(1L)
                .setTitle("cat");

        return new AnimalDTO()
                .setId(id)
                .setUsername(USERNAME_VERIFIED)
                .setAnimalName(name)
                .setAnimalType(type)
                .setGender(GenderType.FEMALE)
                .setBirthdate(null);
    }

    @Test
    void findAllAnimalsByUsernameTest() throws Exception {
        final AnimalDTO animal1 = initAnimal(1L, "Barsik");
        final AnimalDTO animal2 = initAnimal(2L, "Barsik2");
        final Set<AnimalDTO> animals = new LinkedHashSet<>();
        animals.add(animal1);
        animals.add(animal2);

        Mockito.when(animalService.findAllAnimalsByUsername(USERNAME_VERIFIED))
                .thenReturn(animals);

        mvc.perform(get("/api/v1/animal/user/" + USERNAME_VERIFIED)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(animal1))
                .andExpect(jsonPath("$.animals.[1]").value(animal2))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())));
    }
}