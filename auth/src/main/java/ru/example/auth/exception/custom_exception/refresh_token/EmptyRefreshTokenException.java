package ru.example.auth.exception.custom_exception.refresh_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;
import ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmptyRefreshTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = 7041862703294039157L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Refresh-token is empty";
    private static final Integer NUM = E15;

    public EmptyRefreshTokenException() {
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
