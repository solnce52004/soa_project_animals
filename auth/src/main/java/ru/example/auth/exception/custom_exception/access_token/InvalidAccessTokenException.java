package ru.example.auth.exception.custom_exception.access_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidAccessTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 5173918750408378511L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Access-token is is invalid";
    private static final Integer NUM = E1102;

    public InvalidAccessTokenException() {
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
