package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String msg) {
        super(msg);
    }
}
