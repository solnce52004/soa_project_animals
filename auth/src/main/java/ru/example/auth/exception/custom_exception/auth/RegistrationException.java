package ru.example.auth.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistrationException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 877407583157105977L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    public static final String MSG = "Username is already in use";
    private static final Integer NUM = E7;

    public RegistrationException() {
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