package ru.example.animals.exception.custom_exception.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public interface BaseException {
    HttpStatus getHttpStatus();

    String getMsg();

    Integer getNum();
}
