package ru.example.auth.exception.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.exception.custom_exception.*;

@RestControllerAdvice
public class CustomApiExceptionHandler {

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
        return getResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
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
        return getResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFoundException(
            UserNotFoundException ex,
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
//                .setTimestamp(LocalDateTime.now())
                .setDetailMessage(exMsg)
                .setHttpStatus(httpStatus.value())
                .setHttpStatusName(httpStatus);

        return new ResponseEntity<>(new ResponseDTO().setError(error), httpStatus);
    }
}
