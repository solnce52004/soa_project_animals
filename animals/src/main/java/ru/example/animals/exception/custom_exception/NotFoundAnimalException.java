package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.NOT_FOUND,
        reason = "There is no such animal")
public class NotFoundAnimalException extends RuntimeException {
    public NotFoundAnimalException(Long id) {
        super(String.format("Entity with id=%d not found", id));
    }
}
