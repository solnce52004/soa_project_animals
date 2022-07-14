package ru.example.auth.exception.custom_exception.access_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.E1100;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmptyAccessTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 5978384011975764872L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Access-token is empty";
    private static final Integer NUM = E1100;

    public EmptyAccessTokenException() {
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
