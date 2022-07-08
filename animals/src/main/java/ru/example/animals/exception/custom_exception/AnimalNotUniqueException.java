package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AnimalNotUniqueException extends RuntimeException {
    public AnimalNotUniqueException() {
        super("The animal is not unique");
    }
}