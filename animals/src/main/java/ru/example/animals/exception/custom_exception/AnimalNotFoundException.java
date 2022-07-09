package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AnimalNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3956782524869431584L;

    public AnimalNotFoundException() {
        super("Animal not found");
    }
}
