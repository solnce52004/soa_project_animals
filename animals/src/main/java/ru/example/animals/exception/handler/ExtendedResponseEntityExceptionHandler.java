package ru.example.animals.exception.handler;


import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.example.animals.exception.custom_exception.BaseError;
import ru.example.animals.exception.mapper.MapperErrorFieldsByMethodArgumentNotValid;

import java.time.LocalDateTime;
import java.util.List;
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
                .setErrors(
                        new MapperErrorFieldsByMethodArgumentNotValid()
                                .getErrors(ex)
                )
                .setHttpStatus(status.value())
                .setHttpStatusName(status);

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
                .setHttpStatus(status.value())
                .setHttpStatusName(status);

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
                .setHttpStatus(status.value())
                .setHttpStatusName(status);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 400 BAD_REQUEST
     * <p>
     * - запрос вида POST "/dep14/user"
     * - Content-Type application/json
     * - методе параметр @RequestAttribute UserDTO user
     * - в постмане в боди не передается юзер
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex,
            @NonNull HttpHeaders headers,
            HttpStatus status,
            @NonNull WebRequest request
    ) {
        BaseError body = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage("ServletRequestBindingException")
                .setHttpStatus(status.value())
                .setHttpStatusName(status);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 415 UNSUPPORTED_MEDIA_TYPE
     * <p>
     * Content-Type
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        String allowed = "";

        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
            allowed += String.format(
                    CUSTOM_MSG_TEMPLATE_ALLOWED,
                    mediaTypes.toString()
            );
        }

        BaseError body = new BaseError()
                .setTimestamp(LocalDateTime.now())
                .setMessage(String.format(
                        CUSTOM_MSG_TEMPLATE_NOT_SUPPORTED,
                        "(HttpMediaTypeNotSupportedException) (Media) Content-Type",
                        allowed,
                        ex.getContentType()
                ))
                .setHttpStatus(status.value())
                .setHttpStatusName(status);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
