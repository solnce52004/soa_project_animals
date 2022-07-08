package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(String msg) {
        super(msg);
    }
    public UserUnauthorizedException() {
        super("User is UNAUTHORIZED");
    }
}
