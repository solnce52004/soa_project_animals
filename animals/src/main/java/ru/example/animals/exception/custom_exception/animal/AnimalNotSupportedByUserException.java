package ru.example.animals.exception.custom_exception.animal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2101;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AnimalNotSupportedByUserException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 5209444023099399108L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "An animal not supported by current user";
    private static final Integer NUM = E2101;

    public AnimalNotSupportedByUserException() {
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