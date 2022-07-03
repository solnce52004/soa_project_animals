package ru.example.animals.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.exception.custom_exception.BaseError;
import ru.example.animals.exception.custom_exception.NotFoundAnimalException;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;
import ru.example.animals.service.api.FindAnimalByIdApiService;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomApiExceptionHandler {
    /**
     * Будет вызван конструктор переопределенный в NotFoundAnimalException
     * а после обработки перейдет сюда и будут добавлены сообщения в ответ
     */
    @ExceptionHandler(NotFoundAnimalException.class)
    public ResponseEntity<Object> customHandleNotFound(
            NotFoundAnimalException ex,
            WebRequest request
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setHttpStatus(HttpStatus.NOT_FOUND.value())
                .setHttpStatusName(HttpStatus.NOT_FOUND)
                .setMessage(ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO()
                .setInfo(FindAnimalByIdApiService.CODE_404)
                .setError(error)
//                .setAnimals(Collections.singleton(new AnimalDTO()))
                .setHttpStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<Object> customHandleNotFound(
            UserUnauthorizedException ex,
            WebRequest request
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setHttpStatus(HttpStatus.UNAUTHORIZED.value())
                .setHttpStatusName(HttpStatus.UNAUTHORIZED)
                .setMessage(ex.getMessage());

        ResponseDTO responseDTO = new ResponseDTO()
                .setInfo(FindAnimalByIdApiService.CODE_401)
                .setError(error)
                .setHttpStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }
}
