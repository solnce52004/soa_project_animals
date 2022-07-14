package ru.example.auth.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 6180818340283218416L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MSG = "Not found";
    private static final Integer NUM = E9;

    public UserNotFoundException() {
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
