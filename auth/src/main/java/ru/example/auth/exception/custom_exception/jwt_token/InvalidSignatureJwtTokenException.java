package ru.example.auth.exception.custom_exception.jwt_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidSignatureJwtTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 4574151075099875796L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Invalid JWT token signature";
    private static final Integer NUM = E13;

    public InvalidSignatureJwtTokenException() {
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