package ru.example.auth.exception.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.exception.custom_exception.*;

@RestControllerAdvice
@Slf4j
public class CustomApiExceptionHandler {
    @Transient
    @ExceptionHandler(JwtAuthException.class)
    public ResponseEntity<ResponseDTO> handleJwtAuthException(
            JwtAuthException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ResponseDTO> handleRegistrationException(
            RegistrationException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<ResponseDTO> handleAccessTokenException(
            AccessTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ResponseDTO> handleTokenRefreshException(
            RefreshTokenException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFoundException(
            UserNotFoundException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManySignInAttemptsException.class)
    public ResponseEntity<ResponseDTO> handleTooManySignInAttemptsException(
            TooManySignInAttemptsException ex,
            WebRequest request
    ) {
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ResponseDTO> getResponseEntity(
            String exMsg,
            HttpStatus httpStatus
    ) {
        BaseError error = new BaseError()
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
