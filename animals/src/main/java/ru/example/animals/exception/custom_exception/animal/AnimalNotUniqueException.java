package ru.example.animals.exception.custom_exception.animal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2102;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnimalNotUniqueException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -4755037647179264187L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static final String MSG = "The animal is not unique";
    private static final Integer NUM = E2102;

    public AnimalNotUniqueException() {
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