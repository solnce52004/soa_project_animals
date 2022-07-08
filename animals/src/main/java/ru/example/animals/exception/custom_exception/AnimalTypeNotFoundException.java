package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AnimalTypeNotFoundException extends RuntimeException {
    public AnimalTypeNotFoundException() {
        super("Animal type not found");
    }
}