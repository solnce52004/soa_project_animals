package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenException extends RuntimeException {
    private static final long serialVersionUID = -532486355615000091L;

    public RefreshTokenException(String msg) {
        super(msg);
    }
}
