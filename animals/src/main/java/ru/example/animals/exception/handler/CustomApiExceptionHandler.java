package ru.example.animals.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.animals.exception.custom_exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomApiExceptionHandler {

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<BaseError> handleAnimalNotFoundException(
            AnimalNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnimalTypeNotFoundException.class)
    public ResponseEntity<BaseError> handleAnimalTypeNotFoundException(
            AnimalTypeNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<BaseError> handleUserUnauthorizedException(
            UserUnauthorizedException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VerifyTokenException.class)
    public ResponseEntity<BaseError> handleVerifyTokenException(
            VerifyTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<BaseError> getResponseEntity(
            String exMsg,
            HttpStatus httpStatus
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setDetailMessage(exMsg)
                .setHttpStatus(httpStatus.value())
                .setHttpStatusName(httpStatus);

        return new ResponseEntity<>(error, httpStatus);
    }
}
