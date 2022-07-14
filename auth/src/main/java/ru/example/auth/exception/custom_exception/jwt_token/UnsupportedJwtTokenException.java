package ru.example.auth.exception.custom_exception.jwt_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.E14;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnsupportedJwtTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -260152415561383463L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "JWT token is unsupported";
    private static final Integer NUM = E14;

    public UnsupportedJwtTokenException() {
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