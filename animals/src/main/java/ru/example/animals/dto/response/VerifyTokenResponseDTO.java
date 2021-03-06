package ru.example.animals.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.example.animals.exception.custom_exception.util.BaseError;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public final class VerifyTokenResponseDTO implements Serializable {
    private static final long serialVersionUID = -7130621559509375118L;

    private BaseError error;
    private String username;
    private HttpStatus httpStatus;
}