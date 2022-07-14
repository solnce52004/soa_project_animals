package ru.example.auth.exception.custom_exception.refresh_token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.example.auth.exception.custom_exception.util.BaseException;

import static ru.example.auth.exception.custom_exception.util.ExceptionNumberConstant.*;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpiredAndDeletedRefreshTokenException
        extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -3740730406129194645L;
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String MSG = "Refresh-token was expired and deleted";
    private static final Integer NUM = E1401;

    public ExpiredAndDeletedRefreshTokenException() {
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