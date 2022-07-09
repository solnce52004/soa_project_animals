package ru.example.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.example.auth.exception.custom_exception.BaseError;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public final class ResponseDTO implements Serializable {
    private static final long serialVersionUID = 9087218717592666289L;

    private BaseError error;
    private String username;
    private HttpStatus httpStatus;
}
