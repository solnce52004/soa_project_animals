package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 526347590287992312L;

    public UserUnauthorizedException() {
        super("User is UNAUTHORIZED");
    }
}
