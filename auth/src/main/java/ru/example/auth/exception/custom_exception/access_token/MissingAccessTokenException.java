package ru.example.auth.exception.custom_exception.access_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MissingAccessTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -8327517173745887910L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Access-token is missing from the database";
    private static final Integer NUM = E5;

    public MissingAccessTokenException() {
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