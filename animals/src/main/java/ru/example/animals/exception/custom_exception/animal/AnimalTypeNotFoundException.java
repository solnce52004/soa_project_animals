package ru.example.animals.exception.custom_exception.animal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2103;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimalTypeNotFoundException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 459565084379563395L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    public static final String MSG = "Animal type not found";
    private static final Integer NUM = E2103;

    public AnimalTypeNotFoundException() {
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