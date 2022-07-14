package ru.example.auth.exception.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.exception.custom_exception.util.BaseError;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.UnknownException;
import ru.example.auth.exception.custom_exception.access_token.EmptyAccessTokenException;
import ru.example.auth.exception.custom_exception.access_token.ExpiredAndDeletedAccessTokenException;
import ru.example.auth.exception.custom_exception.access_token.InvalidAccessTokenException;
import ru.example.auth.exception.custom_exception.access_token.MissingAccessTokenException;
import ru.example.auth.exception.custom_exception.auth.BadCredentialsAuthException;
import ru.example.auth.exception.custom_exception.auth.RegistrationException;
import ru.example.auth.exception.custom_exception.auth.TooManySignInAttemptsException;
import ru.example.auth.exception.custom_exception.auth.UserNotFoundException;
import ru.example.auth.exception.custom_exception.jwt_token.*;
import ru.example.auth.exception.custom_exception.refresh_token.EmptyRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.ExpiredAndDeletedRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.InvalidRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.MissingRefreshTokenException;

@RestControllerAdvice
@Slf4j
public class CustomApiExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFoundException(
            UserNotFoundException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(TooManySignInAttemptsException.class)
    public ResponseEntity<ResponseDTO> handleTooManySignInAttemptsException(
            TooManySignInAttemptsException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ResponseDTO> handleRegistrationException(
            RegistrationException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(EmptyAccessTokenException.class)
    public ResponseEntity<ResponseDTO> handleEmptyAccessTokenException(
            EmptyAccessTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(MissingAccessTokenException.class)
    public ResponseEntity<ResponseDTO> handleMissingAccessTokenException(
            MissingAccessTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidAccessTokenException(
            InvalidAccessTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(ExpiredAndDeletedAccessTokenException.class)
    public ResponseEntity<ResponseDTO> handleExpiredAndDeletedAccessTokenException(
            ExpiredAndDeletedAccessTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(EmptyRefreshTokenException.class)
    public ResponseEntity<ResponseDTO> handleEmptyRefreshTokenException(
            EmptyRefreshTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(MissingRefreshTokenException.class)
    public ResponseEntity<ResponseDTO> handleMissingRefreshTokenException(
            MissingRefreshTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(ExpiredAndDeletedRefreshTokenException.class)
    public ResponseEntity<ResponseDTO> handleExpiredAndDeletedRefreshTokenException(
            ExpiredAndDeletedRefreshTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(InvalidSignatureJwtTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidSignatureJwtTokenException(
            InvalidSignatureJwtTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidJwtTokenException(
            InvalidJwtTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    public ResponseEntity<ResponseDTO> handleExpiredJwtTokenException(
            ExpiredJwtTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(UnsupportedJwtTokenException.class)
    public ResponseEntity<ResponseDTO> handleUnsupportedJwtTokenException(
            UnsupportedJwtTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(EmptyClaimsJwtTokenException.class)
    public ResponseEntity<ResponseDTO> handleEmptyClaimsJwtTokenException(
            EmptyClaimsJwtTokenException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<ResponseDTO> handleUnknownException(
            UnknownException e,
            WebRequest request) {
        return getResponseEntity(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentialsException(
            BadCredentialsException e,
            WebRequest request) {
        return getResponseEntity(new BadCredentialsAuthException());
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
}
