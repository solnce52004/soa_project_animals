package ru.example.auth.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TooManySignInAttemptsException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 4693580719545293582L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Too many signIn attempts";
    private static final Integer NUM = E1202;

    public TooManySignInAttemptsException() {
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