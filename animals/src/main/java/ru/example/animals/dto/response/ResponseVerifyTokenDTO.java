package ru.example.animals.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import ru.example.animals.exception.custom_exception.BaseError;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class ResponseVerifyTokenDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 110L;

    private BaseError error;
    private String username;
    private HttpStatus httpStatus;
}