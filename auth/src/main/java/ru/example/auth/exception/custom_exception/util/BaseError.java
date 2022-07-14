package ru.example.auth.exception.custom_exception.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseError implements Serializable {
    private static final long serialVersionUID = -1016165956094300653L;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private int httpStatus;
    private HttpStatus httpStatusName;
    private Integer errorNum;
    private String detailMessage;
    private Map<String, Set<String>> errors;
}
