package ru.example.animals.exception.custom_exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2200;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUsernameException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 3917147938820541075L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static final String MSG = "The username in the message is invalid";
    private static final Integer NUM = E2200;

    public InvalidUsernameException() {
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