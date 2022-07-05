package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TooManySignInAttemptsException extends RuntimeException {
    public TooManySignInAttemptsException() {
        super("Too many signIn attempts");
    }
}