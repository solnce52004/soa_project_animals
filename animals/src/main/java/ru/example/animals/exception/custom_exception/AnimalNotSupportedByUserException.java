package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AnimalNotSupportedByUserException extends RuntimeException {
    private static final long serialVersionUID = 8099080090288595795L;

    public AnimalNotSupportedByUserException() {
        super("An animal not supported by current user");
    }
}