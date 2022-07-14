package ru.example.animals.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.exception.custom_exception.util.BaseError;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public final class ResponseDTO implements Serializable {
     private static final long serialVersionUID = -7213006430356957025L;

    private BaseError error;
    private Set<AnimalDTO> animals = new HashSet<>();
    private HttpStatus httpStatus;
}
