package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtAuthException extends RuntimeException {
    private static final long serialVersionUID = 4664051785850093624L;

    public JwtAuthException(String msg) {
        super(msg);
    }
}
