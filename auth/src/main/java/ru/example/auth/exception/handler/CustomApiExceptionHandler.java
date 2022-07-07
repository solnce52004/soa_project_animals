package ru.example.auth.exception.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.auth.dto.ResponseDTO;
import ru.example.auth.exception.custom_exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomApiExceptionHandler {

    @ExceptionHandler(JwtAuthException.class)
    public ResponseEntity<?> handleJwtAuthException(
            JwtAuthException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<?> handleRegistrationException(
            RegistrationException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<Object> handleAccessTokenException(
            AccessTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<Object> handleTokenRefreshException(
            RefreshTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException ex,
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

    @ExceptionHandler(TooManySignInAttemptsException.class)
    public ResponseEntity<Object> handleTooManySignInAttemptsException(
            TooManySignInAttemptsException ex,
            WebRequest request
    ) {
        return getResponseEntity(HttpStatus.FORBIDDEN, ex);
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
