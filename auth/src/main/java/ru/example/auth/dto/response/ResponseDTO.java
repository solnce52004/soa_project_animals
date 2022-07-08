package ru.example.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.example.auth.exception.custom_exception.BaseError;

import javax.persistence.Transient;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class ResponseDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 110L;

    private BaseError error;
    private String username;
    private HttpStatus httpStatus;
}
