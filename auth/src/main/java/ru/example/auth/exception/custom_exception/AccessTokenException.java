package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessTokenException extends RuntimeException {
    private static final long serialVersionUID = -2928329175302756330L;

    public AccessTokenException(String msg) {
        super(msg);
    }
}
