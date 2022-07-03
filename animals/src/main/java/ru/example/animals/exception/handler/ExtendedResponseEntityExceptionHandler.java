package ru.example.animals.exception.handler;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.example.animals.exception.custom_exception.BaseError;
import ru.example.animals.exception.mapper.MapperErrorFieldsByMethodArgumentNotValid;

import java.time.LocalDateTime;
import java.util.Set;

@RestControllerAdvice
public class ExtendedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CUSTOM_MSG_TEMPLATE_ALLOWED = " Allowed types: %s.";
    private static final String CUSTOM_MSG_TEMPLATE_NOT_SUPPORTED = "%s is not supported.%s Type given: [%s]";

    /**
     * 400 BAD_REQUEST
     */
    @Override
    // дескриптор ошибки для @Valid //будет выбрасываться при @valid
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            HttpStatus status,
            @NonNull WebRequest request
    ) {
        BaseError body = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage(ex.getMessage())
                .setHttpStatus(status.value())
//                .setHttpStatusName(status.name())
                .setErrors(
                        new MapperErrorFieldsByMethodArgumentNotValid()
                                .getErrors(ex)
                );

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 404 NOT_FOUND
     * - spring.mvc.throw-exception-if-no-handler-found=true
     * - вызываем несуществующий метод
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            HttpStatus status,
            @NonNull WebRequest request
    ) {
        BaseError body = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage("(NoHandlerFoundException) The handler for the request method was not found")
                .setHttpStatus(status.value());

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 405 METHOD_NOT_ALLOWED
     * - вместо POST - GET
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        pageNotFoundLogger.warn(ex.getMessage());

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String allowed = "";

        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
            allowed += String.format(
                    CUSTOM_MSG_TEMPLATE_ALLOWED,
                    supportedMethods.toString()
            );
        }

        BaseError body = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage(String.format(
                        CUSTOM_MSG_TEMPLATE_NOT_SUPPORTED,
                        "(HttpRequestMethodNotSupportedException) Type method",
                        allowed,
                        ex.getMethod()
                ))
                .setMessage(ex.getMessage())
                .setHttpStatus(status.value());

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
