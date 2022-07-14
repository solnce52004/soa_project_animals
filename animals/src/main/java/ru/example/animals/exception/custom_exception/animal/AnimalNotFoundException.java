package ru.example.animals.exception.custom_exception.animal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;
import ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.*;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimalNotFoundException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 3685488556940478368L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    public static final String MSG = "Animal not found";
    private static final Integer NUM = E2100;

    public AnimalNotFoundException() {
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