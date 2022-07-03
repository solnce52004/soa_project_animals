package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(Long requestUserId) {
        super(String.format("User with id=%d is UNAUTHORIZED", requestUserId));
    }
}
