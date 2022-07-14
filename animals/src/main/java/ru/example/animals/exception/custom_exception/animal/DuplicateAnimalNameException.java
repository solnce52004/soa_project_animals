package ru.example.animals.exception.custom_exception.animal;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.animals.exception.custom_exception.util.BaseException;

import static ru.example.animals.exception.custom_exception.util.ExceptionNumberConstant.E2104;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateAnimalNameException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -5586516725604449141L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static final String MSG = "Duplicate new animal name";
    private static final Integer NUM = E2104;

    public DuplicateAnimalNameException() {
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