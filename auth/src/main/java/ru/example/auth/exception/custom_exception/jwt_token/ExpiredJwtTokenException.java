package ru.example.auth.exception.custom_exception.jwt_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpiredJwtTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -5036380268735914366L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Expired JWT token";
    private static final Integer NUM = E1301;

    public ExpiredJwtTokenException() {
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