package ru.example.auth.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.E6;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadCredentialsAuthException
        extends AuthenticationException implements BaseException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Bad credentials";
    private static final Integer NUM = E6;
    private static final long serialVersionUID = -127831831909529576L;

    public BadCredentialsAuthException() {
        super(MSG);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }

    @Override
    public String getMsg() {
        return MSG;
    }

    @Override
    public Integer getNum() {
        return NUM;
    }
}