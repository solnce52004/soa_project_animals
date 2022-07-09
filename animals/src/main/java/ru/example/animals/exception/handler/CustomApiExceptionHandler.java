package ru.example.animals.exception.handler;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.animals.dto.response.ResponseDTO;
import ru.example.animals.exception.custom_exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomApiExceptionHandler {

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ResponseDTO> handleDataException(
            DataException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleAnimalNotFoundException(
            AnimalNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnimalTypeNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleAnimalTypeNotFoundException(
            AnimalTypeNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ResponseDTO> handleUserUnauthorizedException(
            UserUnauthorizedException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VerifyTokenException.class)
    public ResponseEntity<ResponseDTO> handleVerifyTokenException(
            VerifyTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AnimalNotUniqueException.class)
    public ResponseEntity<ResponseDTO> handleAnimalNotUniqueException(
            AnimalNotUniqueException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<ResponseDTO> handleInvalidUsernameException(
            InvalidUsernameException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponseDTO> getResponseEntity(
            String exMsg,
            HttpStatus httpStatus
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setDetailMessage(exMsg)
                .setHttpStatus(httpStatus.value())
                .setHttpStatusName(httpStatus);

        return new ResponseEntity<>(
                new ResponseDTO()
                        .setError(error)
                        .setHttpStatus(httpStatus),
                httpStatus
        );
    }
}
