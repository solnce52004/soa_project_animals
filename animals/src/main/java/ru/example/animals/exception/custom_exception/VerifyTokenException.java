package ru.example.animals.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class VerifyTokenException extends RuntimeException {
    public VerifyTokenException(String msg) {
        super(msg);
    }
    public VerifyTokenException() {
        super("FORBIDDEN");
    }
}