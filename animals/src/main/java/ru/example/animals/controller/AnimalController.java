package ru.example.animals.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.dto.UserDTO;
import ru.example.animals.service.api.FindAnimalByIdApiService;
import ru.example.animals.service.api.utilservice.ServiceManager;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "Activate/deactivate coffee machine - Turn on/off",
        value = "TurnOnOffController")
@RestController
@RequestMapping(
        path = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AnimalController {
    private final ServiceManager service;

    @Operation(method = "GET",
            description = FindAnimalByIdApiService.DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = FindAnimalByIdApiService.CODE_200,
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "401",
                            description = FindAnimalByIdApiService.CODE_401,
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = FindAnimalByIdApiService.CODE_404,
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findAnimalById(
            @PathVariable("id") Long animalId,
            HttpServletRequest request
    ) {
        final UserDTO curUser = new UserDTO().setUsername(request.getRemoteUser());
        final AnimalDTO animalDTO = new AnimalDTO()
                .setId(animalId)
                .setUser(curUser);

        final ResponseDTO msg = service
                .getResponseService(FindAnimalByIdApiService.class.getName())
                .getResponse(animalDTO);

        return new ResponseEntity<>(msg, msg.getHttpStatus());
    }
}
