package ru.example.auth.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.E1;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 7156896501383355316L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String MSG = "Something went wrong";
    private static final Integer NUM = E1;

    public UnknownException() {
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