package ru.example.animals.service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.entity.Animal;
import ru.example.animals.exception.custom_exception.NotFoundAnimalException;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;
import ru.example.animals.service.api.utilservice.ApiService;
import ru.example.animals.service.modelservice.AnimalService;
import ru.example.animals.service.modelservice.UserService;

import java.util.Collections;

@Service
@Getter
@AllArgsConstructor
@Slf4j
public class FindAnimalByIdApiService implements ApiService {
    private final AnimalService animalService;
    private final UserService userService;

    public static final String DESCRIPTION = "Получить детали любого животного по id";
    public static final String CODE_200 = "Подробная информация о животном получена";
    public static final String CODE_401 = "Животное принадлежит другому пользователю. Либо текущий пользователь не авторизован.";
    public static final String CODE_404 = "Животное с указанным  id не найдено.";

    @Override
    public String getHandlerName() {
        return FindAnimalByIdApiService.class.getName();
    }

    // TODO: тут будет аннотация проверки права пользователя
    @Override
    public ResponseDTO getResponse(AnimalDTO dto) {
        final Animal animal = animalService.findById(dto.getId());
        log.info("FindAnimalByIdApiService: animal {}", animal);

        if(animal.getName() == null){
            throw new NotFoundAnimalException(dto.getId());
        }

        final Long animalUserId = animal.getUser().getId();
        final Long requestUserId = dto.getUser().getId();

        if (!userService.checkAuthAnimalUser(animalUserId, requestUserId)) {
            throw new UserUnauthorizedException(requestUserId);
        }

        return new ResponseDTO()
                .setInfo(CODE_200)
//                .setError(null)
                .setAnimals(Collections.singleton(AnimalDTO.animalMapToDto(animal)))
                .setHttpStatus(HttpStatus.OK);
    }
}
