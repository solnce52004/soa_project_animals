package ru.example.animals.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.exception.custom_exception.AnimalNotFoundException;
import ru.example.animals.exception.custom_exception.BaseError;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomApiExceptionHandler {

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<Object> handleAnimalNotFoundException(
            AnimalNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<Object> handleUserUnauthorizedException(
            UserUnauthorizedException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    private ResponseEntity<Object> getResponseEntity(
            HttpStatus httpStatus,
            RuntimeException ex
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage(ex.getMessage())
                .setHttpStatus(httpStatus.value())
                .setHttpStatusName(httpStatus);

        ResponseDTO responseDTO = new ResponseDTO()
                .setError(error)
                .setHttpStatus(httpStatus);

        return new ResponseEntity<>(responseDTO, httpStatus);
    }
}
