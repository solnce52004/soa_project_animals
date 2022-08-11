package ru.example.animals.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.request.PatchAnimalTypeRequestDTO;
import ru.example.animals.dto.response.ResponseDTO;
import ru.example.animals.exception.custom_exception.auth.InvalidUsernameException;
import ru.example.animals.exception.custom_exception.auth.UserUnauthorizedException;
import ru.example.animals.service.api.VerifyAccessTokenService;
import ru.example.animals.service.modelservice.AnimalService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

@Api(tags = "Managing animal entities", value = "AnimalController")
@RestController
@RequestMapping("/api/v1/animal")
@AllArgsConstructor
public class AnimalController {
    private final AnimalService animalService;
    private final VerifyAccessTokenService verifyAccessTokenService;

    @Operation(method = "GET",
            description = "Find all of animals by username",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all of animals by username was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Animals was not found",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseDTO> findAllAnimalsByUsername(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        verifyUsernames(usernameVerified, username);

        final Set<AnimalDTO> animals = animalService.findAllAnimalsByUsername(username);

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(animals)
                .setHttpStatus(HttpStatus.OK));
    }

    @Operation(method = "GET",
            description = "Find animal by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find animal by id was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Animal was not found",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findAnimalById(
            @PathVariable("id") Long animalId,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        final AnimalDTO dto = animalService.findAnimalById(animalId);
        verifyUsernames(dto.getUsername(), usernameVerified);

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(Collections.singleton(dto))
                .setHttpStatus(HttpStatus.OK));
    }

    @Operation(method = "POST",
            description = "Create animal",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Create animal was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The animal is not unique",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping
    public ResponseEntity<ResponseDTO> createAnimal(
            @Valid @RequestBody AnimalDTO dto,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        final AnimalDTO animal = animalService.create(dto.setUsername(usernameVerified));

        return new ResponseEntity<>(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @Operation(method = "Change animal type",
            description = "Update some animal data",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Change animal type was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> patchAnimalTypeByAnimalId(
            @PathVariable("id") Long animalId,
            @Valid @RequestBody PatchAnimalTypeRequestDTO dto,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        verifyUsernames(dto.getUsername(), usernameVerified);

        final AnimalDTO animal = animalService.patchAnimalTypeByAnimalId(animalId, dto);

        return new ResponseEntity<>(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED);
    }

    @Operation(method = "PUT",
            description = "Update all of animal`s data",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Update animal was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> putAnimal(
            @PathVariable("id") Long animalId,
            @Valid @RequestBody AnimalDTO dto,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        verifyUsernames(dto.getUsername(), usernameVerified);

        final AnimalDTO animal = animalService.put(animalId, dto);

        return new ResponseEntity<>(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED);
    }

    @Operation(method = "DELETE",
            description = "Delete animal",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Delete animal was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAnimal(
            @PathVariable("id") Long animalId,
            HttpServletRequest request
    ) {
        final String usernameVerified = getUsernameVerified(request);
        animalService.delete(animalId, usernameVerified);

        return new ResponseEntity<>(new ResponseDTO()
                .setHttpStatus(HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED);
    }

    ////////
    private String getUsernameVerified(HttpServletRequest request) {
        final String token = request.getHeader(VerifyAccessTokenService.HEADER_NAME_AUTHORIZATION);
        return verifyAccessTokenService.verifyToken(token);
    }

    private void verifyUsernames(
            String usernameVerified,
            String username
    ) {
        if (username == null || username.equals("")) {
            throw new InvalidUsernameException();
        }
        if (!username.equals(usernameVerified)) {
            throw new UserUnauthorizedException();
        }
    }
}
