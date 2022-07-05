package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtAuthException extends AuthenticationException {
    public JwtAuthException(String msg, Throwable e) {
        super(msg, e);
    }
}
