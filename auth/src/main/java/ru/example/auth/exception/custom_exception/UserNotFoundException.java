package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3543077528296544271L;

    public UserNotFoundException() {
        super("User doesn't exists");
    }
}
