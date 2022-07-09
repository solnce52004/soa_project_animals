package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AnimalNotUniqueException extends RuntimeException {
    private static final long serialVersionUID = -1404999677647407118L;

    public AnimalNotUniqueException() {
        super("The animal is not unique");
    }
}