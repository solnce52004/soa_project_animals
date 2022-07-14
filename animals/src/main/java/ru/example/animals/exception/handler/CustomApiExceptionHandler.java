package ru.example.animals.exception.handler;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.animals.dto.response.ResponseDTO;
import ru.example.animals.exception.custom_exception.UnknownException;
import ru.example.animals.exception.custom_exception.animal.*;
import ru.example.animals.exception.custom_exception.auth.InvalidUsernameException;
import ru.example.animals.exception.custom_exception.auth.UserUnauthorizedException;
import ru.example.animals.exception.custom_exception.auth.VerifyTokenException;
import ru.example.animals.exception.custom_exception.util.BaseError;
import ru.example.animals.exception.custom_exception.util.BaseException;

import java.time.LocalDateTime;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.*;

@RestControllerAdvice
public class CustomApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO> handleConstraintViolationException(
            ConstraintViolationException e,
            WebRequest request) {
        return getResponseEntity(E2300, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ResponseDTO> handleDataException(
            DataException e,
            WebRequest request) {
        return getResponseEntity(E2301, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e,
            WebRequest request) {
        return getResponseEntity(E2302, e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    ////
    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleAnimalNotFoundException(
            AnimalNotFoundException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(AnimalTypeNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleAnimalTypeNotFoundException(
            AnimalTypeNotFoundException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ResponseDTO> handleUserUnauthorizedException(
            UserUnauthorizedException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(VerifyTokenException.class)
    public ResponseEntity<ResponseDTO> handleVerifyTokenException(
            VerifyTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(AnimalNotUniqueException.class)
    public ResponseEntity<ResponseDTO> handleAnimalNotUniqueException(
            AnimalNotUniqueException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<ResponseDTO> handleInvalidUsernameException(
            InvalidUsernameException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(AnimalNotSupportedByUserException.class)
    public ResponseEntity<ResponseDTO> handleAnimalNotSupportedByUserException(
            AnimalNotSupportedByUserException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(DuplicateAnimalNameException.class)
    public ResponseEntity<ResponseDTO> handleDuplicateAnimalNameException(
            DuplicateAnimalNameException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<ResponseDTO> handleUnknownException(
            UnknownException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    /////////
    private ResponseEntity<ResponseDTO> getResponseEntity(BaseException e) {
        final HttpStatus httpStatus = e.getHttpStatus();

        BaseError error = new BaseError()
                .setErrorNum(e.getNum())
                .setDetailMessage(e.getMsg())
                .setHttpStatus(httpStatus.value())
                .setHttpStatusName(httpStatus);

        return new ResponseEntity<>(
                new ResponseDTO()
                        .setError(error)
                        .setHttpStatus(httpStatus),
                httpStatus);
    }

    private ResponseEntity<ResponseDTO> getResponseEntity(
            Integer num,
            String exMsg,
            HttpStatus httpStatus
    ) {
        BaseError error = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setErrorNum(num)
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
