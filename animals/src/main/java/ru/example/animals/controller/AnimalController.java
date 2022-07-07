package ru.example.animals.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.service.api.VerifyAccessTokenService;
import ru.example.animals.service.modelservice.AnimalService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

@Api(tags = "Find animal by id", value = "AnimalController")
@RestController
@RequestMapping(
        path = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE)
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
    @GetMapping("/username/{username}")
    public ResponseEntity<ResponseDTO> findAllAnimalsByUsername(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        final String usernameByToken = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        final Set<AnimalDTO> animals = animalService.findAllAnimalsByUsername(usernameByToken);

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
        final String username = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        final AnimalDTO animalById = animalService.findAnimalById(new AnimalDTO()
                .setId(animalId)
                .setUsername(username));

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(Collections.singleton(animalById))
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
                            responseCode = "404",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping
    public ResponseEntity<ResponseDTO> createAnimal(
            @Valid @RequestBody AnimalDTO animalDTO,
            HttpServletRequest request
    ) {
        final String username = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        final AnimalDTO animal = animalService.create(
                animalDTO.setUsername(username));

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.CREATED));
    }

    @Operation(method = "PATCH",
            description = "Update some animal data",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Update some animal data was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateAnimal(
            @PathVariable("id") Long animalId,
            @Valid @RequestBody AnimalDTO animalDTO,
            HttpServletRequest request
    ) {
        final String username = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        final AnimalDTO animal = animalService.update(
                animalId,
                animalDTO.setUsername(username));

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.ACCEPTED));
    }

    @Operation(method = "PUT",
            description = "Update animal",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Update animal was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> putAnimal(
            @PathVariable("id") Long animalId,
            @Valid @RequestBody AnimalDTO animalDTO,
            HttpServletRequest request
    ) {
        final String username = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        final AnimalDTO animal = animalService.update(
                animalId,
                animalDTO.setUsername(username));

        return ResponseEntity.ok(new ResponseDTO()
                .setAnimals(Collections.singleton(animal))
                .setHttpStatus(HttpStatus.ACCEPTED));
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
                            responseCode = "404",
                            description = "User is unauthorized",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAnimal(
            @PathVariable("id") Long animalId,
            HttpServletRequest request
    ) {
        final String username = verifyAccessTokenService
                .getVerifiedResponseDTO(request)
                .getUsername();

        animalService.delete(animalId, username);

        return ResponseEntity.ok(new ResponseDTO()
                .setHttpStatus(HttpStatus.ACCEPTED));
    }
}
