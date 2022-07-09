package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AnimalTypeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5949136158212348455L;

    public AnimalTypeNotFoundException() {
        super("Animal type not found");
    }
}