package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationException extends RuntimeException {
    private static final long serialVersionUID = 2896720716702112474L;

    public RegistrationException(String message) {
        super(message);
    }
}