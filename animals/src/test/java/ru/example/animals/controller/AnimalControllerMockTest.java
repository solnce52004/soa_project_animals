package ru.example.animals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.example.animals.dto.request.PatchAnimalTypeRequestDTO;
import ru.example.animals.service.api.VerifyAccessTokenService;
import ru.example.animals.service.modelservice.AnimalService;
import ru.example.animals.controller.util.AnimalUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.example.animals.controller.util.AnimalUtil.*;

@WebMvcTest(controllers = {AnimalController.class})
class AnimalControllerMockTest {

    private static final String USERNAME_VERIFIED = USERNAME;
    private static final String BEARER_TOKEN = "Bearer token";
    private static final AnimalDTO testAnimal1 = initAnimal(1L, "Barsik");
    private static final AnimalDTO testAnimal2 = initAnimal(2L, "Barsik2");
    private static final String AUTHORIZATION = "Authorization";
    private static final long ANIMAL_ID = 1L;

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

    @Test
    void findAllAnimalsByUsernameTest() throws Exception {
        final Set<AnimalDTO> animals = new LinkedHashSet<>();
        animals.add(testAnimal1);
        animals.add(testAnimal2);

        Mockito.when(animalService.findAllAnimalsByUsername(USERNAME_VERIFIED))
                .thenReturn(animals);

        mvc.perform(get("/api/v1/animal/user/" + USERNAME_VERIFIED)
                .header("Authorization", BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(testAnimal1))
                .andExpect(jsonPath("$.animals.[1]").value(testAnimal2))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())));
    }

    @Test
    void findAnimalById() throws Exception {
        Mockito.when(animalService.findAnimalById(1L))
                .thenReturn(testAnimal1);

        mvc.perform(get("/api/v1/animal/" + ANIMAL_ID)
                .header("Authorization", BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(testAnimal1))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())));
    }

    @Test
    void createAnimal() throws Exception {
        Mockito.when(animalService.create(testAnimal1))
                .thenReturn(testAnimal1);

        mvc.perform(post("/api/v1/animal")
                .header(AUTHORIZATION, BEARER_TOKEN)
                .content(new ObjectMapper().writeValueAsString(testAnimal1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(testAnimal1))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.CREATED.name())))
                .andExpect(status().isCreated());
    }

    @Test
    void patchAnimalTypeByAnimalId() throws Exception {
        final PatchAnimalTypeRequestDTO patch = AnimalUtil.initPatchAnimalTypeRequestDTO(
                2L, "dog");
        final AnimalDTO updated = AnimalUtil.initPatchAnimalTypeResponseDTO(
                ANIMAL_ID, "Barsik",
                2L, "dog");

        Mockito.when(animalService.patchAnimalTypeByAnimalId(ANIMAL_ID, patch))
                .thenReturn(updated);

        mvc.perform(patch("/api/v1/animal/" + ANIMAL_ID)
                .header(AUTHORIZATION, BEARER_TOKEN)
                .content(new ObjectMapper().writeValueAsString(patch))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(updated))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.ACCEPTED.name())))
                .andExpect(status().isAccepted());
    }

    @Test
    void putAnimal() throws Exception {
        Mockito.when(animalService.put(ANIMAL_ID, testAnimal1))
                .thenReturn(testAnimal1);

        mvc.perform(put("/api/v1/animal/" + ANIMAL_ID)
                .header(AUTHORIZATION, BEARER_TOKEN)
                .content(new ObjectMapper().writeValueAsString(testAnimal1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.animals.[0]").value(testAnimal1))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.ACCEPTED.name())))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteAnimal() throws Exception {
        Mockito.doNothing()
                .when(animalService)
                .delete(ANIMAL_ID, USERNAME_VERIFIED);

        mvc.perform(delete("/api/v1/animal/" + ANIMAL_ID)
                .header(AUTHORIZATION, BEARER_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.ACCEPTED.name())))
                .andExpect(status().isAccepted());

        Mockito.verify(animalService, times(1))
                .delete(ANIMAL_ID, USERNAME_VERIFIED);
    }
}