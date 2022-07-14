package ru.example.animals.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2201;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 1094816074639223285L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    public static final String MSG = "User is UNAUTHORIZED";
    private static final Integer NUM = E2201;

    public UserUnauthorizedException() {
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